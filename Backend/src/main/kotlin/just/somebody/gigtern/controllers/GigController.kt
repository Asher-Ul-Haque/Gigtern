package just.somebody.gigtern.controllers

import just.somebody.gigtern.controllers.dtos.requests.ApplicationRequestDTO
import just.somebody.gigtern.controllers.dtos.requests.ApplicationResponseDTO
import just.somebody.gigtern.controllers.dtos.requests.GigRequestDTO
import just.somebody.gigtern.controllers.dtos.responses.GigResponseDTO
import just.somebody.gigtern.service.ApplicationService
import just.somebody.gigtern.service.GigService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import jakarta.validation.Valid
import just.somebody.gigtern.security.SecurityUtils


@RestController
@RequestMapping("/api/v1/gigs")
class GigController(
	private val GIG_SERVICE         : GigService,
	private val APPLICATION_SERVICE : ApplicationService)
{
	// - - - Create a new Gig
	@PostMapping
	fun createGig(@Valid @RequestBody request: GigRequestDTO): ResponseEntity<GigResponseDTO>
	{
		val authenticatedUser = SecurityUtils.getAuthenticatedUser()
		val createdGig        = GIG_SERVICE.createGig(request, authenticatedUser)
		return ResponseEntity(createdGig, HttpStatus.CREATED)
	}

	// - - - Get all available gigs
	@GetMapping
	fun getAllGigs(): ResponseEntity<List<GigResponseDTO>>
	{
		val gigs = GIG_SERVICE.getAllGigs()
		return ResponseEntity(gigs, HttpStatus.OK)
	}

	// - - -Lists all gigs posted by the authenticated Employer.
	@GetMapping("/my")
	fun getMyGigs(): ResponseEntity<List<GigResponseDTO>>
	{
		val authenticatedUser = SecurityUtils.getAuthenticatedUser()
		val gigs              = GIG_SERVICE.getMyGigs(authenticatedUser)
		return ResponseEntity(gigs, HttpStatus.OK)
	}

	// - - -Allows a student to submit an application for a specific gig

	@PostMapping("/{GIG_ID}/apply")
	fun applyForGig(
		@PathVariable       GIG_ID: Long,
		@Valid @RequestBody REQUEST: ApplicationRequestDTO
	): ResponseEntity<ApplicationResponseDTO>
	{
		val authenticatedUser = SecurityUtils.getAuthenticatedUser()
		val application       = APPLICATION_SERVICE.applyForGig(GIG_ID, REQUEST, authenticatedUser)
		return ResponseEntity(application, HttpStatus.CREATED)
	}

	// - - - Retrieves all applications for a specific Gig. Requires Employer ownership.
	@GetMapping("/{GIG_ID}/applications")
	fun getApplicationsForGig(@PathVariable GIG_ID: Long): ResponseEntity<List<ApplicationResponseDTO>>
	{
		val authenticatedUser = SecurityUtils.getAuthenticatedUser()
		val applications      = APPLICATION_SERVICE.getApplicationsForGig(GIG_ID, authenticatedUser)
		return ResponseEntity(applications, HttpStatus.OK)
	}

	// - - - Accepts a specific application and marks the Gig as MATCHED. Requires Employer ownership.
	@PatchMapping("/applications/{APPLICATION_ID}/accept")
	fun acceptApplication(@PathVariable APPLICATION_ID: Long): ResponseEntity<ApplicationResponseDTO>
	{
		val authenticatedUser   = SecurityUtils.getAuthenticatedUser()
		val updatedApplication = APPLICATION_SERVICE.acceptApplication(APPLICATION_ID, authenticatedUser)
		return ResponseEntity(updatedApplication, HttpStatus.OK)
	}
}