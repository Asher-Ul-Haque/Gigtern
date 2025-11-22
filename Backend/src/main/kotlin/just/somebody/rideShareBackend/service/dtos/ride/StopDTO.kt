package just.somebody.rideShareBackend.service.dtos.ride

import jakarta.validation.constraints.Future
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import just.somebody.rideShareBackend.domain.entities.RideEntity
import just.somebody.rideShareBackend.domain.entities.StopEntity
import java.time.LocalDateTime

data class StopDTO(
	@field:NotBlank(message = "Location is required for a stop")
	val location: String,

	@field:Min(value = 1, message = "Sequence must be 1 or greater")
	val sequence: Byte,

	@field:Future(message = "Estimated arrival time must be in the future")
	val estimatedArrivalTime: LocalDateTime,
)

fun StopDTO.toEntity(RIDE: RideEntity) : StopEntity
{
	return StopEntity(
		location              = this.location,
		sequence              = this.sequence,
		estimatedArrivalTime  = this.estimatedArrivalTime,
		ride                  = RIDE)
}

fun StopEntity.toDTO() : StopDTO
{
	return StopDTO(
		location              = this.location,
		sequence              = this.sequence,
		estimatedArrivalTime  = this.estimatedArrivalTime)
}
