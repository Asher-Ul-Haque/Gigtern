package just.somebody.rideShareBackend.domain.repositories

import just.somebody.rideShareBackend.domain.entities.RideEntity
import just.somebody.rideShareBackend.domain.enums.RideStatus
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface RideRepository : JpaRepository<RideEntity, Long>
{
	fun findByDriverIdAndStatus(DRIVER_ID: Int, STATUS: RideStatus): List<RideEntity>

	fun findByStatus(STATUS: RideStatus): List<RideEntity>

	// - - - check if a driver has an active ride
	@Query(
		"""
    	SELECT 	*
      FROM 		rides
      WHERE 	driver_id = :driverId AND 
							status 		= 'ACTIVE'
    """,
		nativeQuery = true)
	fun findActiveRideByDriverId(@Param("driverId") DRIVER_ID: Int): RideEntity?

}