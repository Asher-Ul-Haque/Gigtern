package just.somebody.rideShareBackend.controllers

import jakarta.validation.Valid
import just.somebody.rideShareBackend.service.RideService
import just.somebody.rideShareBackend.service.dtos.ride.RideCreationRequestDTO
import just.somebody.rideShareBackend.service.dtos.ride.RideResponseDTO
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/rides")
class RideController(private val SERVICE: RideService)
{
	// - - - Driver created a new ride lisitng with stops
	@PostMapping
	fun createRide(
		@Valid
		@RequestBody
		REQUEST : RideCreationRequestDTO) : ResponseEntity<RideResponseDTO>
	{
		val response = SERVICE.createRide(REQUEST)
		return ResponseEntity(response, HttpStatus.CREATED)
	}

	@GetMapping("/{ID}")
	fun getRideByID(@PathVariable ID: Long) : Any
	{ TODO() }

  @GetMapping("/{DRIVER_ID}/active")
	fun getActiveRidesByDriver(@PathVariable DRIVER_ID: Int): ResponseEntity<RideResponseDTO>
  { TODO() }
}