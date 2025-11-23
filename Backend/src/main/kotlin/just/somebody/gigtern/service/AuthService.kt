package just.somebody.gigtern.service

import just.somebody.gigtern.controllers.dtos.AuthRequestDTO
import just.somebody.gigtern.controllers.dtos.AuthResponseDTO
import just.somebody.gigtern.domain.entities.EmployerEntity
import just.somebody.gigtern.domain.entities.UserEntity
import just.somebody.gigtern.domain.entities.StudentEntity
import just.somebody.gigtern.domain.enums.Role
import just.somebody.gigtern.domain.repositories.EmployerRepository
import just.somebody.gigtern.domain.repositories.StudentRepository
import just.somebody.gigtern.domain.repositories.UserRepository
import just.somebody.gigtern.security.JwtProvider
import just.somebody.gigtern.service.exceptions.UserAlreadyExistsException
import just.somebody.gigtern.service.exceptions.UserNotFoundException
import just.somebody.gigtern.utils.Logger
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
class AuthService(
	private val USER_REPOSITORY : UserRepository,
	private val EMPLOYER_REPOSITORY: EmployerRepository, // NEW DEPENDENCY
	private val STUDENT_REPOSITORY: StudentRepository,   // NEW DEPENDENCY
	private val JWT_PROVIDER    : JwtProvider,
	private val ENCODER         : PasswordEncoder)
{
	// - - - register a new user
	@Transactional // Ensure both user and profile save together
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

		// --- FIX: Create corresponding profile entity based on role ---
		when (savedUser.role) {
			Role.EMPLOYER -> {
				val employerProfile = EmployerEntity(
					user = savedUser,
					pointOfContact = savedUser.name, // Use name as POC for MVP default
					aadharKycUrl = null
				)
				EMPLOYER_REPOSITORY.save(employerProfile)
				Logger.LOG_INFO("[Auth Service] : Created Employer Profile for User ${savedUser.id}")
			}
			Role.STUDENT -> {
				val studentProfile = StudentEntity(
					user = savedUser,
					university = null, // Set nulls for MVP
					skills = emptyList(),
					studentIdProofUrl = null
				)
				STUDENT_REPOSITORY.save(studentProfile)
				Logger.LOG_INFO("[Auth Service] : Created Student Profile for User ${savedUser.id}")
			}
		}

		Logger.LOG_INFO("[Auth Service] : Saved new User : ${REQUEST.name} with ID ${savedUser.id}")
		// ----------------------------------------------------------------

		// FIX: Ensure ID is Long and generate token
		val userId = savedUser.id
			?: throw IllegalStateException("Database failed to assign ID to new UserEntity.")

		val JWT : String = JWT_PROVIDER.generateToken(userId, savedUser.role)
		return savedUser.toResponseDTO(JWT, "Registration successful")
	}

	// - - - login a user (no change needed here)
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

		// FIX: Ensure ID is Long and generate token
		val userId = user.id
			?: throw IllegalStateException("User found but ID is null.")


		val jwt = JWT_PROVIDER.generateToken(userId, user.role)
		return user.toResponseDTO(jwt, "Login successful")
	}
}