package just.somebody.gigtern.service

import just.somebody.gigtern.controllers.dtos.requests.ApplicationRequestDTO
import just.somebody.gigtern.controllers.dtos.requests.ApplicationResponseDTO
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

// - - - Service layer for handling Gig applications from students and managing the match process.
@Service
class ApplicationService(
	private val APPLICATION_REPO  : ApplicationRepository,
	private val GIG_REPO          : GigRepository,
	private val STUDENT_REPO      : StudentRepository
)
{
	// - - - Student Application Submission
	@Transactional
	fun applyForGig(GIG_ID: Long, REQUEST: ApplicationRequestDTO, AUTHENTICATED_USER: UserEntity): ApplicationResponseDTO
	{
		// - - - Role Check: Only students can apply
		if (AUTHENTICATED_USER.role != Role.STUDENT)
		{
			Logger.LOG_ERROR("[Application Service] User ${AUTHENTICATED_USER.id} tried to apply without STUDENT role.")
			throw AuthorizationException("Only Students can apply for gigs.")
		}
		val student = STUDENT_REPO.findByUser(AUTHENTICATED_USER)
			?: throw ResourceNotFoundException("Student profile not found for user ID ${AUTHENTICATED_USER.id}.")
		val gig = GIG_REPO.findById(GIG_ID)
			.orElseThrow { ResourceNotFoundException("Gig not found with ID: $GIG_ID") }
		if (gig.status != GigStatus.OPEN)
		{
			throw ApplicationException("Gig is not open for applications. Current status: ${gig.status.name}")
		}

		val newApplication    = REQUEST.toEntity(gig, student)
		val savedApplication  = APPLICATION_REPO.save(newApplication)

		Logger.LOG_INFO("[Application Service] Student ${student.id} applied for Gig ${GIG_ID}. Application ID: ${savedApplication.id}.")
		return savedApplication.toResponseDTO()
	}


	// - - - Employer Views Applications
	fun getApplicationsForGig(GIG_ID : Long, AUTHENTICATED_USER_ENTITY : UserEntity): List<ApplicationResponseDTO>
	{
		// - - - Fetch Gig and check ownership
		val gig      = GIG_REPO.findById(GIG_ID).orElseThrow { ResourceNotFoundException("Gig not found with ID: $GIG_ID") }
		val employer = gig.employer

		// - - - Check if the authenticated user owns this gig
		if (AUTHENTICATED_USER_ENTITY.id != employer.user.id)
		{
			Logger.LOG_ERROR("[Application Service] User ${AUTHENTICATED_USER_ENTITY.id} attempted to view applications for Gig ${GIG_ID} without ownership.")
			throw AuthorizationException("You are not authorized to view applications for this gig.")
		}

		// - - - Fetch all applications for the gig
		return APPLICATION_REPO.findAllByGigId(GIG_ID)
			.map { it.toResponseDTO() }
	}

	// - - - Employer Accepts Application
	@Transactional
	fun acceptApplication(APPLICATION_ID: Long, AUTHENTICATED_USER: UserEntity): ApplicationResponseDTO
	{
		// - - - Fetch Application and Gig
		val application = APPLICATION_REPO.findById(APPLICATION_ID)
			.orElseThrow { ResourceNotFoundException("Application not found with ID: $APPLICATION_ID") }

		val gig = application.gig

		// - - - Ownership Check
		if (AUTHENTICATED_USER.id != gig.employer.user.id)
		{
			Logger.LOG_ERROR("[Application Service] User ${AUTHENTICATED_USER.id} attempted to accept application ${APPLICATION_ID} without ownership.")
			throw AuthorizationException("You are not authorized to accept applications for this gig.")
		}

		// - - - Status Checks
		if (gig.status != GigStatus.OPEN)
		{
			throw ApplicationException("Cannot accept application. Gig is already matched or closed (Status: ${gig.status.name}).")
		}
		if (application.status != ApplicationStatus.APPLIED)
		{
			throw ApplicationException("Application status is not 'APPLIED' (Status: ${application.status.name}).")
		}

		// - - - Update Statuses - - -

		// - - - Update Application Status
		val updatedApplication = application.copy(status = ApplicationStatus.ACCEPTED)
		APPLICATION_REPO.save(updatedApplication)

		// - - - Update Gig Status
		gig.status = GigStatus.MATCHED
		GIG_REPO.save(gig)

		Logger.LOG_WARNING("[Application Service] : MVP Assumption : trigger rejections for all other applications")
		Logger.LOG_INFO("[Application Service] Application $APPLICATION_ID ACCEPTED. Gig ${gig.id} matched.")
		return updatedApplication.toResponseDTO()
	}
}