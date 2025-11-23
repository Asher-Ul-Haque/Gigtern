package just.somebody.gigtern.domain.repositories

import just.somebody.gigtern.domain.entities.EmployerEntity
import just.somebody.gigtern.domain.entities.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface EmployerRepository : JpaRepository<EmployerEntity, Long>
{
	fun findByUser(USER: UserEntity): EmployerEntity?
}