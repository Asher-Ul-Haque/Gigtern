package just.somebody.rideShareBackend.service.dtos.search

import just.somebody.rideShareBackend.service.dtos.ride.RideResponseDTO

data class SearchResultDTO(
	val ride                    : RideResponseDTO,
	val driverName              : String,
	val driverGender            : String,
	val driverReputation        : Float,
	val estimatedContribution   : Double,
	val existingPassengers      : List<PassengerSummaryDTO>)