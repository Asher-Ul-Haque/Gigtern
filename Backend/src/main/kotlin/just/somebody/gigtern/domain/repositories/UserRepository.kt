package just.somebody.gigtern.domain.repositories

import just.somebody.gigtern.domain.entities.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<UserEntity, Long>
{
	fun findByEmail  (EMAIL: String): UserEntity?
	fun existsByEmail(EMAIL: String): Boolean
}
