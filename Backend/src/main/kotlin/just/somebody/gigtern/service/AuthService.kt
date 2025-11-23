package just.somebody.gigtern.service

import just.somebody.gigtern.controllers.dtos.requests.AuthRequestDTO
import just.somebody.gigtern.controllers.dtos.responses.AuthResponseDTO
import just.somebody.gigtern.domain.entities.EmployerEntity
import just.somebody.gigtern.domain.entities.UserEntity
import just.somebody.gigtern.domain.entities.StudentEntity
import just.somebody.gigtern.domain.enums.Role
import just.somebody.gigtern.domain.repositories.EmployerRepository
import just.somebody.gigtern.domain.repositories.StudentRepository
import just.somebody.gigtern.domain.repositories.UserRepository
import just.somebody.gigtern.security.JwtProvider
import just.somebody.gigtern.utils.exceptions.UserAlreadyExistsException
import just.somebody.gigtern.utils.exceptions.UserNotFoundException
import just.somebody.gigtern.utils.Logger
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
class AuthService(
	private val USER_REPOSITORY     : UserRepository,
	private val EMPLOYER_REPOSITORY : EmployerRepository,
	private val STUDENT_REPOSITORY  : StudentRepository,
	private val JWT_PROVIDER        : JwtProvider,
	private val ENCODER             : PasswordEncoder)
{
	// - - - register a new user
	@Transactional
	fun registerUser(REQUEST : AuthRequestDTO): AuthResponseDTO
	{
		// - - - Check if user already exists
		if (USER_REPOSITORY.findByEmail(REQUEST.email) != null)
		{
			Logger.LOG_ERROR("[Auth Service] : User with email already exists")
			throw UserAlreadyExistsException("User with email ${REQUEST.email} already exists.")
		}

		// - - - Hash the password and create the new entity
		val hashedPassword = ENCODER.encode(REQUEST.password)
		val newUser        = REQUEST.toEntity(hashedPassword)

		// - - - Save base User to database
		val savedUser = USER_REPOSITORY.save(newUser)

		// - - - Create corresponding profile entity based on role
		when (savedUser.role)
		{
			Role.EMPLOYER ->
				{
					val employerProfile = EmployerEntity(
						user            = savedUser,
						pointOfContact  = savedUser.name, // - - - TODO: Change default
						aadharKycUrl    = null)
					EMPLOYER_REPOSITORY.save(employerProfile)
					Logger.LOG_WARNING("[Auth Service] : MVP assumption here")
					Logger.LOG_INFO("[Auth Service] : Created Employer Profile for User ${savedUser.id}")
				}
			Role.STUDENT ->
				{
					val studentProfile = StudentEntity(
						user              = savedUser,
						university        = null, // - - - TODO: Change default
						skills            = emptyList(),
						studentIdProofUrl = null)
					STUDENT_REPOSITORY.save(studentProfile)
					Logger.LOG_WARNING("[Auth Service] : MVP assumption here")
					Logger.LOG_INFO("[Auth Service] : Created Student Profile for User ${savedUser.id}")
				}
		}

		Logger.LOG_INFO("[Auth Service] : Saved new User : ${REQUEST.name} with ID ${savedUser.id}")

		val jwt : String = JWT_PROVIDER.generateToken(savedUser.id, savedUser.role)
		return savedUser.toResponseDTO(jwt, "Registration successful")
	}

	// - - - login a user
	fun loginUser(REQUEST: AuthRequestDTO): AuthResponseDTO
	{
		// - - - Find user by email
		val user: UserEntity? = USER_REPOSITORY.findByEmail(REQUEST.email)
		if (user == null)
		{
			Logger.LOG_ERROR("[Auth Service] : User not found for email : ${REQUEST.email}")
			throw UserNotFoundException("User not found for email: ${REQUEST.email}")
		}

		// - - - Verify password
		if (!ENCODER.matches(REQUEST.password, user.passwordHash))
		{
			Logger.LOG_ERROR("[Auth Service] : Invalid credentials")
			throw SecurityException("Invalid credentials.")
		}

		val jwt = JWT_PROVIDER.generateToken(user.id, user.role)
		return user.toResponseDTO(jwt, "Login successful")
	}
}