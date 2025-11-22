package just.somebody.rideShareBackend.service.dtos.ride

import jakarta.validation.Valid
import jakarta.validation.constraints.Future
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty
import just.somebody.rideShareBackend.domain.entities.RideEntity
import just.somebody.rideShareBackend.domain.enums.RideStatus
import just.somebody.rideShareBackend.service.validations.auth.ValidRollNo
import just.somebody.rideShareBackend.service.validations.ride.ValidDepartureTime
import java.time.LocalDateTime

data class RideCreationRequestDTO(
	@field:ValidRollNo
	val driverRollNo: Int,

	@field:NotBlank(message = "Origin Location is required")
	val originLocation: String,

	@field:ValidDepartureTime
	val departureTime: LocalDateTime,

	@field:Min(
		value   = 1,
		message = "Max seats must be at least 1")
	val maxSeats: Byte)

fun RideCreationRequestDTO.toRideEntity() : RideEntity
{
	return RideEntity(
		driverId        = this.driverRollNo,
		originLocation  = this.originLocation,
		departureTime   = this.departureTime,
		maxSeats        = this.maxSeats,
		bookedSeats     = 0,
		status          = RideStatus.ACTIVE)
}