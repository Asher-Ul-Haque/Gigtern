package just.somebody.rideShareBackend.domain.repositories

import just.somebody.rideShareBackend.domain.entities.RideEntity
import just.somebody.rideShareBackend.domain.entities.RideRequestEntity
import just.somebody.rideShareBackend.domain.enums.RequestStatus
import just.somebody.rideShareBackend.domain.enums.RideStatus
import org.springframework.data.jpa.repository.JpaRepository

interface RideRequestRepository : JpaRepository<RideRequestEntity, Long>
{
	fun findByRideIdAndRequestStatus(RIDE_ID: Long, STATUS: RequestStatus): List<RideRequestEntity>

	fun findByPassengerId(PASSENGER_ID: Int): List<RideRequestEntity>
}