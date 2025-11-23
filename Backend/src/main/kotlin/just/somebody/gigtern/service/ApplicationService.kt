package just.somebody.gigtern.service

import just.somebody.gigtern.controllers.dtos.ApplicationRequestDTO
import just.somebody.gigtern.controllers.dtos.ApplicationResponseDTO
import just.somebody.gigtern.controllers.dtos.toResponseDTO
import just.somebody.gigtern.domain.repositories.ApplicationRepository
import just.somebody.gigtern.utils.exceptions.AuthorizationException

import just.somebody.gigtern.domain.entities.UserEntity
import just.somebody.gigtern.domain.enums.ApplicationStatus
import just.somebody.gigtern.domain.enums.GigStatus
import just.somebody.gigtern.domain.enums.Role
import just.somebody.gigtern.domain.repositories.GigRepository
import just.somebody.gigtern.domain.repositories.StudentRepository

import just.somebody.gigtern.utils.Logger
import just.somebody.gigtern.utils.exceptions.ApplicationException
import just.somebody.gigtern.utils.exceptions.ResourceNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * Service layer for handling Gig applications from students.
 */

/**
 * Service layer for handling Gig applications from students and managing the match process.
 */
@Service
class ApplicationService(
	private val applicationRepository: ApplicationRepository,
	private val gigRepository: GigRepository,
	private val studentRepository: StudentRepository
) {

	// --- Student Application Submission (Existing) ---
	@Transactional
	fun applyForGig(gigId: Long, request: ApplicationRequestDTO, authenticatedUser: UserEntity): ApplicationResponseDTO {
		// 1. Role Check: Only students can apply
		if (authenticatedUser.role != Role.STUDENT) {
			Logger.LOG_ERROR("[Application Service] User ${authenticatedUser.id} tried to apply without STUDENT role.")
			throw AuthorizationException("Only Students can apply for gigs.")
		}
		val student = studentRepository.findByUser(authenticatedUser)
			?: throw ResourceNotFoundException("Student profile not found for user ID ${authenticatedUser.id}.")
		val gig = gigRepository.findById(gigId)
			.orElseThrow { ResourceNotFoundException("Gig not found with ID: $gigId") }
		if (gig.status != GigStatus.OPEN) {
			throw ApplicationException("Gig is not open for applications. Current status: ${gig.status.name}")
		}

		val newApplication = request.toEntity(gig, student)
		val savedApplication = applicationRepository.save(newApplication)

		Logger.LOG_INFO("[Application Service] Student ${student.id} applied for Gig ${gigId}. Application ID: ${savedApplication.id}.")
		return savedApplication.toResponseDTO()
	}


	// --- NEW: Employer Views Applications ---

	/**
	 * GET: Retrieves all applications for a specific gig.
	 * @param gigId The ID of the gig.
	 * @param authenticatedUser The user requesting the list (must be the gig's Employer).
	 * @return List of Application details.
	 */
	fun getApplicationsForGig(gigId: Long, authenticatedUser: UserEntity): List<ApplicationResponseDTO> {
		// 1. Fetch Gig and check ownership
		val gig = gigRepository.findById(gigId)
			.orElseThrow { ResourceNotFoundException("Gig not found with ID: $gigId") }

		val employer = gig.employer // Get the linked employer

		// Check if the authenticated user owns this gig
		if (authenticatedUser.id != employer.user.id) {
			Logger.LOG_ERROR("[Application Service] User ${authenticatedUser.id} attempted to view applications for Gig ${gigId} without ownership.")
			throw AuthorizationException("You are not authorized to view applications for this gig.")
		}

		// 2. Fetch all applications for the gig
		return applicationRepository.findAllByGigId(gigId)
			.map { it.toResponseDTO() }
	}


	// --- NEW: Employer Accepts Application ---

	/**
	 * PATCH: Accepts a specific application, finalizing the match.
	 * @param applicationId The ID of the application to accept.
	 * @param authenticatedUser The user accepting the application (must be the gig's Employer).
	 * @return The updated Application details.
	 */
	@Transactional
	fun acceptApplication(applicationId: Long, authenticatedUser: UserEntity): ApplicationResponseDTO {
		// 1. Fetch Application and Gig
		val application = applicationRepository.findById(applicationId)
			.orElseThrow { ResourceNotFoundException("Application not found with ID: $applicationId") }

		val gig = application.gig

		// 2. Ownership Check (re-use logic from viewing)
		if (authenticatedUser.id != gig.employer.user.id) {
			Logger.LOG_ERROR("[Application Service] User ${authenticatedUser.id} attempted to accept application ${applicationId} without ownership.")
			throw AuthorizationException("You are not authorized to accept applications for this gig.")
		}

		// 3. Status Checks
		if (gig.status != GigStatus.OPEN) {
			throw ApplicationException("Cannot accept application. Gig is already matched or closed (Status: ${gig.status.name}).")
		}
		if (application.status != ApplicationStatus.APPLIED) {
			throw ApplicationException("Application status is not 'APPLIED' (Status: ${application.status.name}).")
		}

		// 4. Update Statuses

		// a) Update Application Status
		val updatedApplication = application.copy(status = ApplicationStatus.ACCEPTED)
		applicationRepository.save(updatedApplication)

		// b) Update Gig Status (This is the match!)
		gig.status = GigStatus.MATCHED
		gigRepository.save(gig)

		// V2: In a real app, this would also trigger rejection for all other applications.

		Logger.LOG_INFO("[Application Service] Application ${applicationId} ACCEPTED. Gig ${gig.id} matched.")
		return updatedApplication.toResponseDTO()
	}
}