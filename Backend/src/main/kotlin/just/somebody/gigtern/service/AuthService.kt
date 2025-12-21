package just.somebody.gigtern.service

import jakarta.transaction.Transactional
import just.somebody.gigtern.controller.dtos.requests.LoginRequest
import just.somebody.gigtern.controller.dtos.requests.RegisterRequest
import just.somebody.gigtern.controller.dtos.response.TokenResponse
import just.somebody.gigtern.domain.entities.UserEntity
import just.somebody.gigtern.domain.enums.Role
import just.somebody.gigtern.domain.repositories.UserRepository
import just.somebody.gigtern.security.JwtProvider
import just.somebody.gigtern.utils.Logger
import just.somebody.gigtern.utils.exceptions.UserAlreadyExistsException
import just.somebody.gigtern.utils.exceptions.UserNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthService(
	private val USER_REPO   : UserRepository,
	private val ENCODER     : PasswordEncoder,
	private val JWT_PROVIDER: JwtProvider)
{
	// - - - register a new user
	@Transactional
	fun registerUser(REQUEST: RegisterRequest): TokenResponse
	{
		// - - - check if user already exists
		if (USER_REPO.findByEmail(REQUEST.email) != null)
		{
			Logger.LOG_ERROR("[Auth Service] User with email ${REQUEST.email} already exists")
			throw UserAlreadyExistsException("User with email ${REQUEST.email}")
		}

		// - - - Hash the password and create the new entity
		val user = USER_REPO.save(
			UserEntity(
				name          = REQUEST.name,
				email         = REQUEST.email,
				passwordHash  = ENCODER.encode(REQUEST.password)!!
			)
		)
		return TokenResponse(JWT_PROVIDER.generateToken(user.id, Role.STUDENT))
	}

	@Transactional
	fun login(REQUEST: LoginRequest): TokenResponse
	{
		val user = USER_REPO.findByEmail(REQUEST.email) ?: throw UserNotFoundException("Invalid credentials")
		if (!ENCODER.matches(REQUEST.password, user.passwordHash)) throw UserNotFoundException("Invalid credentials")

		return TokenResponse(JWT_PROVIDER.generateToken(user.id, user.role))
	}
}