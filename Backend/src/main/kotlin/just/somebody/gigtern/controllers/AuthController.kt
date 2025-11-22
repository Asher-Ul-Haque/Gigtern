package just.somebody.gigtern.controllers

import jakarta.validation.Valid
import just.somebody.gigtern.controllers.dtos.AuthRequestDTO
import just.somebody.gigtern.controllers.dtos.AuthResponseDTO
import just.somebody.gigtern.domain.entities.UserEntity
import just.somebody.gigtern.domain.repositories.UserRepository
import just.somebody.gigtern.service.AuthService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api/v1/auth")
class AuthController(private val SERVICE : AuthService)
{
	@PostMapping("/register")
	fun register(
		@Valid
		@RequestBody REQUEST: AuthRequestDTO): ResponseEntity<AuthResponseDTO>
	{
		return ResponseEntity(SERVICE.registerUser(REQUEST), HttpStatus.CREATED)
	}

	@PostMapping("/login")
	fun login(
		@Valid
		@RequestBody REQUEST: AuthRequestDTO): ResponseEntity<AuthResponseDTO>
	{
		return ResponseEntity(SERVICE.loginUser(REQUEST), HttpStatus.ACCEPTED)
	}
}