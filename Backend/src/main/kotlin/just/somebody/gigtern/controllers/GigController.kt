package just.somebody.gigtern.controllers

import com.fasterxml.jackson.databind.exc.InvalidNullException
import jakarta.validation.Valid
import just.somebody.gigtern.controllers.dtos.GigRequestDTO
import just.somebody.gigtern.controllers.dtos.GigResponseDTO
import just.somebody.gigtern.domain.entities.UserEntity
import just.somebody.gigtern.service.GigService
import just.somebody.gigtern.utils.exceptions.AuthorizationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/gigs")
class GigController(private val GIG_SERVICE: GigService)
{
	private fun getAuthenticatedUser(): UserEntity {
		// 1. Get the Authentication object from the SecurityContext
		val authentication = SecurityContextHolder.getContext().authentication

		// 2. Check if the user is authenticated (i.e., not an anonymous user)
		if (authentication == null || authentication.principal == "anonymousUser" || authentication.principal !is UserEntity) {
			// Throw a 401/403 handled by GlobalExceptionHandler
			throw AuthorizationException("Authentication token is missing or invalid.")
		}

		// 3. Cast the principal object to our UserEntity type
		return authentication.principal as UserEntity
	}

	/**
	 * POST /api/v1/gigs
	 * Creates a new Gig. Requires EMPLOYER role.
	 */
	@PostMapping
	fun createGig(
		@Valid @RequestBody request: GigRequestDTO
		// Removed @AuthenticationPrincipal argument
	): ResponseEntity<GigResponseDTO> {

		// Manual extraction
		val authenticatedUser = getAuthenticatedUser()

		// The service layer handles the Role check (Role.EMPLOYER)
		val createdGig = GIG_SERVICE.createGig(request, authenticatedUser)
		return ResponseEntity(createdGig, HttpStatus.CREATED)
	}

	@GetMapping
	fun getAllGigs(): ResponseEntity<List<GigResponseDTO>>
	{
		val gigs = GIG_SERVICE.getAllGigs()
		return ResponseEntity(gigs, HttpStatus.OK)
	}

	// - - - Lists all gigs posted by the authenticated Employer. Requires EMPLOYER role.
	@GetMapping("/my")
	fun getMyGigs(@AuthenticationPrincipal USER: UserEntity): ResponseEntity<List<GigResponseDTO>>
	{
		val gigs = GIG_SERVICE.getMyGigs(USER)
		return ResponseEntity(gigs, HttpStatus.OK)
	}
}