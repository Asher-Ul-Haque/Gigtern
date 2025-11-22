package just.somebody.rideShareBackend.controllers

import jakarta.servlet.http.HttpServletRequest
import jakarta.validation.Valid
import just.somebody.rideShareBackend.service.AuthService
import just.somebody.rideShareBackend.service.dtos.auth.LoginRequestDTO
import just.somebody.rideShareBackend.service.dtos.auth.SignupRequestDTO
import just.somebody.rideShareBackend.service.dtos.auth.UserResponseDTO
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/auth")
class AuthController(private val SERVICE: AuthService)
{
	// - - - Registers a new user and performs the mock IIITD verification.
	@PostMapping("/signup")
	fun signup(
		@Valid
		@RequestBody
		REQUEST: SignupRequestDTO,
		SERVLET: HttpServletRequest): ResponseEntity<UserResponseDTO>
	{
		val ipAddr        = extractClientIP(SERVLET)
		val userResponse  = SERVICE.signup(REQUEST, ipAddr)
		return ResponseEntity(userResponse, HttpStatus.CREATED)  // - - - 201 Created
	}

	// - - - Logs in a verified user by Roll No.
	@PostMapping("/login")
	fun login(
		@Valid
		@RequestBody
		REQUEST : LoginRequestDTO): ResponseEntity<UserResponseDTO>
	{
		val userResponse  = SERVICE.login(REQUEST)
		return ResponseEntity(userResponse, HttpStatus.OK) // - - - 200 OK
	}

	private fun extractClientIP(SERVLET: HttpServletRequest): String
	{
		val header = SERVLET.getHeader("X-Forwarded-For")
		if (header != null && header.isNotBlank())  return header.split(",")[0].trim()
		else                                        return SERVLET.remoteAddr;
	}
}