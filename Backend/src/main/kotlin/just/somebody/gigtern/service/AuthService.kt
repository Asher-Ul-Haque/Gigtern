package just.somebody.gigtern.service

import just.somebody.gigtern.controllers.dtos.AuthRequestDTO
import just.somebody.gigtern.controllers.dtos.AuthResponseDTO
import just.somebody.gigtern.domain.entities.UserEntity
import just.somebody.gigtern.domain.repositories.UserRepository
import just.somebody.gigtern.security.JwtProvider
import just.somebody.gigtern.service.exceptions.UserAlreadyExistsException
import just.somebody.gigtern.service.exceptions.UserNotFoundException
import just.somebody.gigtern.utils.Logger
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service


@Service
class AuthService(
	private val USER_REPOSITORY : UserRepository,
	private val JWT_PROVIDER    : JwtProvider,
	private val ENCODER         : PasswordEncoder)
{
	// - - - register a new user
	fun registerUser(REQUEST : AuthRequestDTO): AuthResponseDTO
	{
		// - - - Check if user already exists
		if (USER_REPOSITORY.findByEmail(REQUEST.email) != null)
		{
			Logger.LOG_ERROR("[Auth Service] : User with email already exists")
			throw UserAlreadyExistsException("User with email $REQUEST.email  already exists.")
		}

		// - - - Hash the password adn crate the new entity
		val hashedPassword = ENCODER.encode(REQUEST.password)
		val newUser        = REQUEST.toEntity(hashedPassword)

		// - - - Save to database
		USER_REPOSITORY.save(newUser)
		Logger.LOG_INFO("[Auth Service] : Saving a new User : ${REQUEST.name}")

		val JWT : String = JWT_PROVIDER.generateToken(newUser.id, newUser.role)
		return newUser.toResponseDTO(JWT)
	}

	// - - - login a user
	fun loginUser(REQUEST: AuthRequestDTO): AuthResponseDTO
	{
		// - - - Find user by email
		val user: UserEntity? = USER_REPOSITORY.findByEmail(REQUEST.email)
		if (user == null)
		{
			Logger.LOG_ERROR("[Auth Service] : User not found for email : ${REQUEST.email}")
			throw UserNotFoundException("User not found for email: $REQUEST.email")
		}

		// - - - Verify password
		if (!ENCODER.matches(REQUEST.password, user.passwordHash))
		{
			Logger.LOG_ERROR("[Auth Service] : Invalid credentials")
			throw SecurityException("Invalid credentials.")
		}

		val jwt = JWT_PROVIDER.generateToken(user.id, user.role)
		return user.toResponseDTO(jwt)
	}
}