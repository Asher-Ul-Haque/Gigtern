package just.somebody.gigtern.domain.repositories

import just.somebody.gigtern.domain.entities.StudentEntity
import just.somebody.gigtern.domain.entities.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface StudentRepository : JpaRepository<StudentEntity, Long> {
	/**
	 * Finds the Student profile linked to a specific base User entity.
	 * @param user The base UserEntity.
	 * @return The StudentEntity associated with the user, or null.
	 */
	fun findByUser(user: UserEntity): StudentEntity?
}