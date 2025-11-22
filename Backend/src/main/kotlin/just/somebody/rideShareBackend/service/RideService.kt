package just.somebody.rideShareBackend.service

import jakarta.transaction.Transactional
import just.somebody.rideShareBackend.domain.repositories.RideRepository
import just.somebody.rideShareBackend.domain.repositories.UserRepository
import just.somebody.rideShareBackend.service.dtos.ride.RequestActionDTO
import just.somebody.rideShareBackend.service.dtos.ride.RideCreationRequestDTO
import just.somebody.rideShareBackend.service.dtos.ride.RideResponseDTO
import just.somebody.rideShareBackend.service.dtos.ride.toEntity
import just.somebody.rideShareBackend.service.dtos.ride.toResponseDTO
import just.somebody.rideShareBackend.service.dtos.ride.toRideEntity
import just.somebody.rideShareBackend.utils.Logger
import org.springframework.stereotype.Service

@Service
class RideService(
	private val RIDE_REPO: RideRepository,
	private val USER_REPO: UserRepository)
{
	fun createRide(REQUEST: RideCreationRequestDTO) : RideResponseDTO
	{
		val driverRollNo = REQUEST.driverRollNo
		Logger.LOG_TRACE("[Ride Service] : Attempting to create ride for driver: $driverRollNo")

		// - - - validation check
		val driver = USER_REPO.findById(driverRollNo).orElse(null)
		if (driver == null)
		{
			Logger.LOG_WARNING("[Ride Service] : Ride creation failed because Driver with roll no $driverRollNo was not found in the database")
			throw IllegalArgumentException("Driver must be a verified IIITD user to list a ride")
		}

		// - - - check if a rider has an active ride already
		val activeRide = RIDE_REPO.findActiveRideByDriverId(REQUEST.driverRollNo)
		if (activeRide != null)
		{
			Logger.LOG_ERROR("[Ride Service] : Ride creation failed: Driver $driverRollNo already has active ride ${activeRide.id}.")
			throw IllegalStateException("Driver can only have one active ride listing at a time.")
		}

		// - - - map DTO to Ride Entity
		val newRide  = REQUEST.toRideEntity()

		// - - - save the ride
		val savedRide = RIDE_REPO.save(newRide)
		Logger.LOG_INFO("[Ride Service] : Ride ID ${savedRide.id} successfully created with ${savedRide.stops.size} stops.")

		return savedRide.toResponseDTO()
	}

	// --- P4.1: Passenger Creates Request ---

	// Passenger requests a seat segment. Performs capacity and stop validity checks.
	@Transactional
	fun createRequest(request: RequestActionDTO, passengerRollNo: Long): RideResponseDTO {
		Logger.LOG_INFO("Passenger ${passengerRollNo} requesting seat on ride ${request.requestId}")

		val ride = RIDE_REPO.findById(request.requestId).orElseThrow {
			IllegalArgumentException("Ride not found.")
		}

		// 1. Capacity Check: Ensure seat is available
		if (ride.bookedSeats >= ride.maxSeats) {
			throw IllegalStateException("Ride is fully booked.")
		}

		// 2. Stop Validity Check: Ensure stops exist and are in the correct sequence
		if (!isStopSequenceValid(request.rideId, request.pickupStopId, request.dropOffStopId)) {
			throw IllegalArgumentException("Invalid pickup/dropoff stop sequence for ride ${request.rideId}.")
		}

		// 3. Prevent duplicate or simultaneous requests
		val existingRequests = requestRepository.findByPassengerId(passengerRollNoStr)
		if (existingRequests.any { it.rideId == request.rideId && it.status != RequestStatus.REJECTED }) {
			throw IllegalStateException("A pending or accepted request already exists for this ride.")
		}

		// 4. Calculate Contribution (Monetization)
		val quotedContribution = calculateMockContribution(request.rideId, request.pickupStopId, request.dropOffStopId)

		// 5. Create Request Entity
		val newRequest = RideRequest(
			rideId = request.rideId,
			passengerId = passengerRollNoStr,
			pickupStopId = request.pickupStopId,
			dropOffStopId = request.dropOffStopId,
			quotedContribution = quotedContribution,
			status = RequestStatus.PENDING,
			paymentStatus = PaymentStatus.PENDING_PAYMENT
		)

		val savedRequest = requestRepository.save(newRequest)

		Logger.LOG_INFO("Request ${savedRequest.requestId} created for ride ${request.rideId}. Status: PENDING.")
		return mapToRideRequestResponse(savedRequest)
	}

	private fun isStopSequenceValid(rideId: Long, pickupStopId: Long, dropOffStopId: Long): Boolean {
		val stops = stopRepository.findByRideRideIdOrderBySequence(rideId)
		val pickupStop = stops.find { it.stopId == pickupStopId }
		val dropOffStop = stops.find { it.stopId == dropOffStopId }

		if (pickupStop == null || dropOffStop == null) return false

		// Ensure pickup comes before dropoff
		return dropOffStop.sequence > pickupStop.sequence
	}

	// --- P4.2: Driver Action (Accept/Reject) ---

	/**
	 * Driver accepts or rejects a pending request.
	 */
	@Transactional
	fun handleDriverAction(actionRequest: RequestAction, driverRollNoStr: String): RideRequestResponse {
		val request = requestRepository.findById(actionRequest.requestId).orElseThrow {
			IllegalArgumentException("Ride request ${actionRequest.requestId} not found.")
		}

		val ride = rideRepository.findById(request.rideId).orElseThrow {
			IllegalStateException("Referenced ride not found.")
		}

		// 1. Authorization Check: Ensure the user taking action is the ride's driver
		if (ride.driverId != driverRollNoStr) {
			Logger.LOG_ERROR("Unauthorized action on request ${request.requestId}: Driver $driverRollNoStr is not the ride owner.")
			throw IllegalStateException("Unauthorized: Only the ride owner can accept or reject requests.")
		}

		// 2. Status Check: Only PENDING requests can be acted upon
		if (request.status != RequestStatus.PENDING) {
			throw IllegalStateException("Request is already ${request.status.name}.")
		}

		val action = actionRequest.action.uppercase()

		return when (action) {
			"ACCEPT" -> {
				// Check capacity one last time
				if (ride.bookedSeats >= ride.maxSeats) {
					throw IllegalStateException("Cannot accept: Ride is now fully booked.")
				}

				request.status = RequestStatus.ACCEPTED
				// Crucial: Increment booked seats on the Ride entity
				ride.bookedSeats += 1
				rideRepository.save(ride) // Persist seat change

				Logger.LOG_INFO("Request ${request.requestId} accepted. Booked seats: ${ride.bookedSeats}/${ride.maxSeats}")
				mapToRideRequestResponse(requestRepository.save(request))
			}
			"REJECT" -> {
				request.status = RequestStatus.REJECTED
				Logger.LOG_INFO("Request ${request.requestId} rejected.")
				mapToRideRequestResponse(requestRepository.save(request))
			}
			else -> throw IllegalArgumentException("Invalid action: $action.")
		}
	}
}