package just.somebody.gigtern.service

import just.somebody.gigtern.domain.entities.UserEntity
import just.somebody.gigtern.domain.repositories.UserRepository
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class UserDetailsService(private val USER_REPO: UserRepository) : UserDetailsService
{
	private fun UserEntity.toUserDetails() : User
	{
		return User(
			this.email,
			this.passwordHash,
			listOf(SimpleGrantedAuthority("ROLE_${role.name}"))
		)
	}

	fun loadUserById(ID: Long): UserDetails?
	{
		val userOptional = USER_REPO.findById(ID)
		if (userOptional.isEmpty) return null
		return userOptional.get().toUserDetails()
	}

	override fun loadUserByUsername(EMAIL: String): UserDetails
	{
		return USER_REPO.findByEmail(EMAIL)
			?.toUserDetails()
			?: throw UsernameNotFoundException("User $EMAIL not found")
	}
}