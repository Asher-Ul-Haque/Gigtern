package just.somebody.gigtern.domain.repositories

import just.somebody.gigtern.domain.entities.GigEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface GigRepository : JpaRepository<GigEntity, Long>
{
	fun findAllByEmployerId(employerId: Long): List<GigEntity>
}