package just.somebody.gigtern.controllers

import just.somebody.gigtern.controllers.dtos.responses.UserProfileResponseDTO
import just.somebody.gigtern.security.SecurityUtils
import just.somebody.gigtern.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/api/v1/users")
class UserController(
	private val USER_SERVICE      : UserService)
{
	// - - - retrieve the user's full details
	@GetMapping("/me")
	fun getMyProfile(): ResponseEntity<UserProfileResponseDTO>
	{
		val authenticatedUser = SecurityUtils.getAuthenticatedUser()
		val profile           = USER_SERVICE.getMyProfile(authenticatedUser)
		return ResponseEntity(profile, HttpStatus.OK)
	}

	// - - - TODO: Partial profile
}