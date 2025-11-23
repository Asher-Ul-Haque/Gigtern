package just.somebody.gigtern.domain.repositories

import just.somebody.gigtern.domain.entities.StudentEntity
import just.somebody.gigtern.domain.entities.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

	@Repository
	interface StudentRepository : JpaRepository<StudentEntity, UUID>
	{
		fun findByEmail(EMAIL: String): StudentEntity?
	}