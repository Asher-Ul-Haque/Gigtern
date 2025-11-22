package just.somebody.rideShareBackend.service.dtos.ride

import just.somebody.rideShareBackend.domain.entities.RideEntity
import just.somebody.rideShareBackend.domain.enums.RideStatus
import java.time.LocalDateTime


data class RideResponseDTO(
	val rideID        : Long,
	val driverRollNo  : Int,
	val originLocation: String,
	val departureTime : LocalDateTime,
	val maxSeats      : Byte,
	val bookedSeats   : Byte,
	val status        : RideStatus,
	val stops         : List<StopDTO>)

fun RideEntity.toResponseDTO() : RideResponseDTO
{
	return RideResponseDTO(
		rideID          = this.id,
		driverRollNo    = this.driverId,
		originLocation  = this.originLocation,
		departureTime   = this.departureTime,
		maxSeats        = this.maxSeats,
		bookedSeats     = this.bookedSeats,
		status          = this.status,
		stops           = this.stops.map { stop -> stop.toDTO() }
	)
}
