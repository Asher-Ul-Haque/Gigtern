package just.somebody.gigtern.domain.repositories

import just.somebody.gigtern.domain.entities.ApplicationEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ApplicationRepository : JpaRepository<ApplicationEntity, Long> {
	/**
	 * Finds all applications associated with a specific Gig ID.
	 * @param gigId The ID of the Gig.
	 * @return List of ApplicationEntity.
	 */
	fun findAllByGigId(gigId: Long): List<ApplicationEntity>
}