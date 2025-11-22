package just.somebody.rideShareBackend.service

import just.somebody.rideShareBackend.domain.entities.RideEntity
import just.somebody.rideShareBackend.domain.enums.RequestStatus
import just.somebody.rideShareBackend.domain.enums.RideStatus
import just.somebody.rideShareBackend.domain.enums.UserGender
import just.somebody.rideShareBackend.domain.repositories.RideRepository
import just.somebody.rideShareBackend.domain.repositories.RideRequestRepository
import just.somebody.rideShareBackend.domain.repositories.StopRepository
import just.somebody.rideShareBackend.domain.repositories.UserRepository
import just.somebody.rideShareBackend.service.dtos.ride.RideResponseDTO
import just.somebody.rideShareBackend.service.dtos.ride.StopDTO
import just.somebody.rideShareBackend.service.dtos.search.PassengerSummaryDTO
import just.somebody.rideShareBackend.service.dtos.search.RideSearchRequestDTO
import just.somebody.rideShareBackend.service.dtos.search.SearchResultDTO
import just.somebody.rideShareBackend.utils.Logger
import org.springframework.stereotype.Service
import java.time.Duration
import kotlin.math.abs

@Service
class SearchService(
	private val RIDE_REPO     : RideRepository,
	private val USER_REPO     : UserRepository,
	private val STOP_REPO     : StopRepository,
	private val REQUEST_REPO  : RideRequestRepository)
{
	private val SEARCH_WINDOW_MINUTES = 60L
	private val AVG_COST_PER_KM       = 8.0f

	fun searchRides(request: RideSearchRequestDTO): List<SearchResultDTO>
	{
		Logger.LOG_INFO("[Search Service] : Starting ride search: Pickup=${request.pickupLocation}, Dropoff=${request.dropOffLocation}")

		val allActiveRides  = RIDE_REPO.findByStatus(RideStatus.ACTIVE)
		val filteredRides   = mutableListOf<RideEntity>()

		// - - - Filter by Route and Time Window
		for (ride in allActiveRides)
		{
			val stops       = STOP_REPO.findByRideIdOrderBySequence(ride.id)
			val pickupStop  = stops.find { it.location == request.pickupLocation }
			val dropOffStop = stops.find { it.location == request.dropOffLocation }

			if (pickupStop != null && dropOffStop != null && pickupStop.sequence < dropOffStop.sequence)
			{
				val timeDifference = Duration.between(request.desiredTime, ride.departureTime).toMinutes()
				if (abs(timeDifference) <= SEARCH_WINDOW_MINUTES) filteredRides.add(ride)
			}
		}

		// - - - Enrich, Prioritize, and Map
		val searchResults = filteredRides.mapNotNull()
		{ ride ->
			val driver = USER_REPO.findById(ride.driverId).orElse(null)
				?: return@mapNotNull null

			// - - - Calculate existing passenger details
			val existingPassengersSummaries = getExistingPassengerSummaries(ride)
			val contribution                = calculateMockContribution(
				ride.id,
				request.pickupLocation,
				request.dropOffLocation)

			// - - - Map to SearchResult DTO
			SearchResultDTO(
				ride                  = mapToRideResponse(ride),
				driverName            = driver.name,
				driverGender          = driver.gender.name,
				driverReputation      = driver.reputationScore,
				estimatedContribution = contribution,
				existingPassengers    = existingPassengersSummaries)
		}

		// - - - Safety Prioritization
		return prioritizeResults(searchResults, request.isFemalePassenger)
	}

	// - - - Lady Safety
	private fun prioritizeResults(
		RESULTS             : List<SearchResultDTO>,
		IS_FEMALE_PASSENGER : Boolean): List<SearchResultDTO>
	{
		// - - - For male passengers just sort by reputation descending
		if (!IS_FEMALE_PASSENGER) return RESULTS.sortedByDescending { it.driverReputation }

		// - - - Female Passenger Prioritization - - -
		return RESULTS.sortedWith(compareByDescending<SearchResultDTO>
		// - - - first prioritize women drivers
		{ result -> result.driverGender == UserGender.FEMALE.name }.thenByDescending()

		// - - - then cars that already have women passengers
		{ result -> result.existingPassengers.any { it.gender == UserGender.FEMALE } }.thenByDescending()

		// - - - then male drivers sorted by reputation
		{ result -> result.driverReputation }.thenBy()

		// - - - sort by departure time (earliest first)
		{ result -> result.ride.departureTime })
	}

	// - - - Fetches details of all accepted passengers for a given ride and returns non-sensitive summaries, including Name and Roll Number for campus-level trust.
	private fun getExistingPassengerSummaries(RIDE : RideEntity): List<PassengerSummaryDTO>
	{
		val acceptedRequests = REQUEST_REPO.findByRideIdAndRequestStatus(RIDE.id, RequestStatus.ACCEPTED)

		return acceptedRequests.mapNotNull()
		{ request ->
			// - - - Look up the full User entity for gender
			val passenger = USER_REPO.findById(request.passengerId).orElse(null)

			if (passenger != null)
			{
				PassengerSummaryDTO(
					rollNo  = passenger.rollNo,
					name    = passenger.name,
					gender  = passenger.gender)
			}
			else null
		}
	}

	// - - - Helper Functions
	private fun calculateMockContribution(RIDE_ID: Long, PICKUP: String, DROP_OFF: String): Double
	{
		// - - - Simple mock calculation: Contribution is based on the number of stops between pickup and dropoff.
		val stops         = STOP_REPO.findByRideIdOrderBySequence(RIDE_ID)
		val pickupIndex   = stops.indexOfFirst { it.location == PICKUP }
		val dropOffIndex  = stops.indexOfFirst { it.location == DROP_OFF }

		if (pickupIndex < 0 || dropOffIndex < 0 || dropOffIndex <= pickupIndex) { return 0.0 }

		val segments = dropOffIndex - pickupIndex
		return (150.0 + (segments * 50.0)) // - - - Mock fixed price structure
	}

	private fun mapToRideResponse(ride: RideEntity): RideResponseDTO
	{
		val stopDTOs = ride.stops.map()
		{ stop ->
			StopDTO(
				location              = stop.location,
				sequence              = stop.sequence,
				estimatedArrivalTime  = stop.estimatedArrivalTime)
		}.sortedBy { it.sequence }

		return RideResponseDTO(
			rideID          = ride.id,
			driverRollNo    = ride.driverId,
			originLocation  = ride.originLocation,
			departureTime   = ride.departureTime,
			maxSeats        = ride.maxSeats,
			bookedSeats     = ride.bookedSeats,
			status          = ride.status,
			stops           = stopDTOs)
	}
}