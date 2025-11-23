package just.somebody.gigtern.service

import just.somebody.gigtern.utils.exceptions.ResourceNotFoundException
import just.somebody.gigtern.controllers.dtos.responses.UserProfileResponseDTO
import just.somebody.gigtern.domain.entities.UserEntity
import just.somebody.gigtern.domain.enums.Role
import just.somebody.gigtern.domain.repositories.EmployerRepository
import just.somebody.gigtern.domain.repositories.StudentRepository
import just.somebody.gigtern.domain.repositories.UserRepository
import just.somebody.gigtern.utils.Logger
import org.springframework.stereotype.Service

// - - - Service dedicated to retrieving and managing User profiles.
@Service
class UserService(
	private val USER_REPO     : UserRepository,
	private val STUDENT_REPO  : StudentRepository,
	private val EMPLOYER_REPO : EmployerRepository
)
{
	// - - - get full profile
	fun getMyProfile(AUTHENTICATED_USER: UserEntity): UserProfileResponseDTO
	{
		// - - - Find the base UserEntity again to ensure we have the latest version
		val user = USER_REPO.findById(AUTHENTICATED_USER.id)
			.orElseThrow()
			{
				Logger.LOG_ERROR("[User Service] Auth user ID ${AUTHENTICATED_USER.id} not found in DB.")
				ResourceNotFoundException("Authenticated user profile not found.")
			}

		// - - - Initialize optional profiles
		var studentProfile =
			if (user.role == Role.STUDENT) STUDENT_REPO.findByUser(user)
			else                           null

		var employerProfile =
			if (user.role == Role.EMPLOYER) EMPLOYER_REPO.findByUser(user)
			else                            null

		// - - - Ensure the required role-specific profile exists
		if (user.role == Role.STUDENT && studentProfile == null)
		{
			Logger.LOG_ERROR("[User Service] Student ${user.id} has no linked StudentEntity.")
			throw ResourceNotFoundException("Student profile data is incomplete.")
		}
		if (user.role == Role.EMPLOYER && employerProfile == null)
		{
			Logger.LOG_ERROR("[User Service] Employer ${user.id} has no linked EmployerEntity.")
			throw ResourceNotFoundException("Employer profile data is incomplete.")
		}

		Logger.LOG_INFO("[User Service] Profile successfully retrieved for user ${user.id} (${user.role}).")

		// - - - Convert and return the consolidated DTO
		return user.toProfileResponseDTO(studentProfile, employerProfile)
	}

	// - - - TODO: Add updateProfile method here
}