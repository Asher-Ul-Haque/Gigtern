package just.somebody.rideShareBackend.service

import just.somebody.rideShareBackend.domain.entities.UserEntity
import just.somebody.rideShareBackend.domain.repositories.UserRepository
import just.somebody.rideShareBackend.security.JwtProvider
import just.somebody.rideShareBackend.service.dtos.auth.LoginRequestDTO
import just.somebody.rideShareBackend.service.dtos.auth.SignupRequestDTO
import just.somebody.rideShareBackend.service.dtos.auth.UserResponseDTO
import just.somebody.rideShareBackend.service.dtos.auth.toResponseDTO
import just.somebody.rideShareBackend.service.dtos.auth.toEntity
import just.somebody.rideShareBackend.utils.Logger
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class AuthService(
	private val USER_REPOSITORY : UserRepository,
	private val REST_TEMPLATE   : RestTemplate,
	private val JWT_PROVIDER    : JwtProvider)
{
	// - - - URL for mock verification (IIITD homepage)
	private val VERIFICATION_URL = "https://www.iiitd.ac.in/"

	private fun mockVerifyIIITDUser(ROLL_NO: Int, IP_ADDR: String)
	{
		Logger.LOG_DEBUG("[Auth Service]: IP_ADDR : $IP_ADDR to be verified against $ROLL_NO")
		Logger.LOG_WARNING("[Auth Service]: Simulating mock verification by making a network call. Considering 200 as OK")
		try
		{
			val response = REST_TEMPLATE.getForEntity(VERIFICATION_URL, String::class.java)
			if (response.statusCode != HttpStatus.OK)
			{
				throw IllegalStateException("User identity could not be verified via IIIT-D heck.")
			}
		}
		catch (e: Exception)
		{
			throw IllegalStateException("User identity could not be verified via IIIT-D heck.")
		}
	}

	fun signup(REQUEST: SignupRequestDTO, IP_ADDR: String): UserResponseDTO
	{
		Logger.LOG_TRACE("[Auth Service] : Attempting signup for Roll No: ${REQUEST.rollNo}")

		if (USER_REPOSITORY.findById(REQUEST.rollNo).isPresent) throw IllegalArgumentException("User with roll number ${REQUEST.rollNo} already exists.")

		// - - - Mock Verification Check
		mockVerifyIIITDUser(REQUEST.rollNo, IP_ADDR)

		// - - - Create and Save User Entity
		val jwt       = JWT_PROVIDER.generateToken(REQUEST.rollNo, REQUEST.role)
		val savedUser = USER_REPOSITORY.save(REQUEST.toEntity())

		Logger.LOG_INFO("[Auth Service] : New user ${savedUser.rollNo} signed up and verified.")
		return savedUser.toResponseDTO(jwt)
	}

	// - - - Handles user login. Requires user to exist
	fun login(REQUEST: LoginRequestDTO): UserResponseDTO
	{
		Logger.LOG_TRACE("[Auth Service] : Attempting login for Roll No: ${REQUEST.rollNo}")

		val user = USER_REPOSITORY.findById(REQUEST.rollNo).orElse(null) ?:
			{
				Logger.LOG_ERROR("[Auth Service] : Login failed: User not found or not verified")
				throw IllegalStateException("Login failed: User not found or not verified.")
			}

		user as UserEntity
		val jwt = JWT_PROVIDER.generateToken(user.rollNo, user.role)

		Logger.LOG_INFO("[Auth Service] : Login successful for user ${REQUEST.rollNo}.")
		return (user).toResponseDTO(jwt)
	}
}