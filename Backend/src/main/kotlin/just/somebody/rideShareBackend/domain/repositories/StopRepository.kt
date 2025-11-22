package just.somebody.rideShareBackend.domain.repositories

import just.somebody.rideShareBackend.domain.entities.StopEntity
import org.springframework.data.jpa.repository.JpaRepository

interface StopRepository : JpaRepository<StopEntity, Long>
{
	fun findByRideIdOrderBySequence(RIDE_ID: Long): List<StopEntity>
}