package just.somebody.gigtern.controller

import jakarta.validation.Valid
import just.somebody.gigtern.controller.dtos.requests.LoginRequest
import just.somebody.gigtern.controller.dtos.requests.RegisterRequest
import just.somebody.gigtern.controller.dtos.response.TokenResponse
import just.somebody.gigtern.service.AuthService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/auth")
class AuthController(private val AUTH_SERVICE: AuthService)
{
	@PostMapping("/register")
	fun register(
		@Valid
		@RequestBody
		REQUEST: RegisterRequest): ResponseEntity<TokenResponse>
	{ return ResponseEntity(AUTH_SERVICE.registerUser(REQUEST), HttpStatus.CREATED) }

	@PostMapping("/login")
	fun login(
		@Valid
		@RequestBody
		REQUEST: LoginRequest): ResponseEntity<TokenResponse>
	{ return ResponseEntity(AUTH_SERVICE.login(REQUEST), HttpStatus.ACCEPTED) }

	@GetMapping("/hello")
	fun hello(): String = "Auth says hello, this should be public"

	@GetMapping("/hi")
	fun hi(): String = "Auth says hi, this should not be public"
}