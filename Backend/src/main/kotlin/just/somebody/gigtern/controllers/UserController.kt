package just.somebody.gigtern.controllers

import just.somebody.gigtern.utils.exceptions.AuthorizationException

import just.somebody.gigtern.controllers.dtos.UserProfileResponseDTO
import just.somebody.gigtern.domain.entities.UserEntity
import just.somebody.gigtern.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*

/**
 * Controller for User Profile management (GET /api/v1/users/me).
 */
@RestController
@RequestMapping("/api/v1/users")
class UserController(
	private val userService: UserService
) {
	/**
	 * Helper function to manually retrieve the authenticated UserEntity from the context.
	 */
	private fun getAuthenticatedUser(): UserEntity {
		val authentication = SecurityContextHolder.getContext().authentication
		if (authentication == null || authentication.principal == "anonymousUser" || authentication.principal !is UserEntity) {
			throw AuthorizationException("Authentication token is missing or invalid.")
		}
		return authentication.principal as UserEntity
	}

	/**
	 * GET /api/v1/users/me
	 * Retrieves the authenticated user's full profile details.
	 */
	@GetMapping("/me")
	fun getMyProfile(): ResponseEntity<UserProfileResponseDTO> {
		val authenticatedUser = getAuthenticatedUser()
		val profile = userService.getMyProfile(authenticatedUser)
		return ResponseEntity(profile, HttpStatus.OK)
	}

	// V2: Add PUT /me endpoint here
}