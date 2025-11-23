package just.somebody.gigtern.api.controller

import just.somebody.gigtern.controllers.dtos.ApplicationRequestDTO
import just.somebody.gigtern.controllers.dtos.ApplicationResponseDTO
import just.somebody.gigtern.controllers.dtos.GigRequestDTO
import just.somebody.gigtern.controllers.dtos.GigResponseDTO
import just.somebody.gigtern.domain.entities.UserEntity
import just.somebody.gigtern.service.ApplicationService
import just.somebody.gigtern.service.GigService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import jakarta.validation.Valid
import just.somebody.gigtern.utils.Logger // Added Logger import
import just.somebody.gigtern.utils.exceptions.AuthorizationException

/**
 * Controller for Phase 2, 3, & 4: Gig Posting, Discovery, Application, and Matching.
 */
@RestController
@RequestMapping("/api/v1/gigs")
class GigController(
	private val gigService: GigService,
	private val applicationService: ApplicationService
) {
	/**
	 * Helper function to manually retrieve the authenticated UserEntity from the context.
	 * This is the robust way to access the principal in Kotlin controllers.
	 */
	private fun getAuthenticatedUser(): UserEntity {
		val authentication = SecurityContextHolder.getContext().authentication

		if (authentication == null || authentication.principal == "anonymousUser" || authentication.principal !is UserEntity) {
			// This exception will be caught by the GlobalExceptionHandler and returned as a 401/403.
			throw AuthorizationException("Authentication token is missing or invalid.")
		}

		return authentication.principal as UserEntity
	}

	// ----------------------------------------------------------------------------------
	// Phase 2: Gig Creation and Management (Employer)
	// ----------------------------------------------------------------------------------

	/**
	 * POST /api/v1/gigs
	 * Creates a new Gig. Requires EMPLOYER role.
	 */
	@PostMapping
	fun createGig(
		@Valid @RequestBody request: GigRequestDTO
	): ResponseEntity<GigResponseDTO> {
		val authenticatedUser = getAuthenticatedUser()
		val createdGig = gigService.createGig(request, authenticatedUser)
		return ResponseEntity(createdGig, HttpStatus.CREATED)
	}

	/**
	 * GET /api/v1/gigs
	 * Public endpoint: Lists all available gigs.
	 */
	@GetMapping
	fun getAllGigs(): ResponseEntity<List<GigResponseDTO>> {
		val gigs = gigService.getAllGigs()
		return ResponseEntity(gigs, HttpStatus.OK)
	}

	/**
	 * GET /api/v1/gigs/my
	 * Lists all gigs posted by the authenticated Employer. Requires EMPLOYER role.
	 */
	@GetMapping("/my")
	fun getMyGigs(): ResponseEntity<List<GigResponseDTO>> {
		val authenticatedUser = getAuthenticatedUser()
		val gigs = gigService.getMyGigs(authenticatedUser)
		return ResponseEntity(gigs, HttpStatus.OK)
	}

	// ----------------------------------------------------------------------------------
	// Phase 3: Student Application Submission (Student)
	// ----------------------------------------------------------------------------------

	/**
	 * POST /api/v1/gigs/{gigId}/apply
	 * Allows a student to submit an application for a specific gig. Requires STUDENT role.
	 */
	@PostMapping("/{gigId}/apply")
	fun applyForGig(
		@PathVariable gigId: Long,
		@Valid @RequestBody request: ApplicationRequestDTO
	): ResponseEntity<ApplicationResponseDTO> {
		val authenticatedUser = getAuthenticatedUser()
		val application = applicationService.applyForGig(gigId, request, authenticatedUser)
		return ResponseEntity(application, HttpStatus.CREATED)
	}

	// ----------------------------------------------------------------------------------
	// Phase 4: Match and Booking (Employer)
	// ----------------------------------------------------------------------------------

	/**
	 * GET /api/v1/gigs/{gigId}/applications
	 * Retrieves all applications for a specific Gig. Requires Employer ownership.
	 */
	@GetMapping("/{gigId}/applications")
	fun getApplicationsForGig(
		@PathVariable gigId: Long
	): ResponseEntity<List<ApplicationResponseDTO>> {
		val authenticatedUser = getAuthenticatedUser()
		val applications = applicationService.getApplicationsForGig(gigId, authenticatedUser)
		return ResponseEntity(applications, HttpStatus.OK)
	}

	/**
	 * PATCH /api/v1/applications/{applicationId}/accept
	 * Accepts a specific application and marks the Gig as MATCHED. Requires Employer ownership.
	 */
	@PatchMapping("/applications/{applicationId}/accept")
	fun acceptApplication(
		@PathVariable applicationId: Long
	): ResponseEntity<ApplicationResponseDTO> {
		val authenticatedUser = getAuthenticatedUser()
		val updatedApplication = applicationService.acceptApplication(applicationId, authenticatedUser)
		return ResponseEntity(updatedApplication, HttpStatus.OK)
	}
}