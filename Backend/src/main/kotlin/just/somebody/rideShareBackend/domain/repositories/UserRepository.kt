package just.somebody.rideShareBackend.domain.repositories

import just.somebody.rideShareBackend.domain.entities.UserEntity
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<UserEntity, Int>
{
	fun findByEmail(EMAIL: String): UserEntity?
}