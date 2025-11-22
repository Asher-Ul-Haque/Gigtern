package just.somebody.rideShareBackend.service.dtos.search

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import just.somebody.rideShareBackend.service.validations.ride.ValidDepartureTime
import java.time.LocalDateTime

data class RideSearchRequestDTO(
	@field:NotBlank(message = "Pickup location is required.")
	val pickupLocation: String,

	@field:NotBlank(message = "Dropoff location is required.")
	val dropOffLocation: String,

	@field:ValidDepartureTime
	val desiredTime: LocalDateTime,

	val isFemalePassenger: Boolean
)