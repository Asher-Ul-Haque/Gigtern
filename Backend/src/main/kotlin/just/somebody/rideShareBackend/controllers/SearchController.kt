package just.somebody.rideShareBackend.controllers

import jakarta.validation.Valid
import just.somebody.rideShareBackend.service.RideService
import just.somebody.rideShareBackend.service.SearchService
import just.somebody.rideShareBackend.service.dtos.ride.RideCreationRequestDTO
import just.somebody.rideShareBackend.service.dtos.ride.RideResponseDTO
import just.somebody.rideShareBackend.service.dtos.search.RideSearchRequestDTO
import just.somebody.rideShareBackend.service.dtos.search.SearchResultDTO
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime

@RestController
@RequestMapping("/api/v1/rides")
class SearchController(
	private val RIDE_SERVICE    : RideService,
	private val SEARCH_SERVICE  : SearchService)
{
	// - - - Driver created a new ride listing with stops
	@PostMapping
	fun createRide(@Valid @RequestBody REQUEST : RideCreationRequestDTO): ResponseEntity<RideResponseDTO>
	{
		val response = RIDE_SERVICE.createRide(REQUEST)
		return ResponseEntity(response, HttpStatus.CREATED)
	}

	// - - - Public endpoint for passenger search
	@GetMapping("/search")
	fun searchRides(
		@RequestParam PICKUP_LOCATION   : String,
		@RequestParam DROP_OFF_LOCATION : String,
		@RequestParam DESIRED_TIME      : LocalDateTime,
		@RequestParam IS_FEMALE         : Boolean
	): ResponseEntity<List<SearchResultDTO>>
	{
		// - - - Build the Search Request DTO
		val request = RideSearchRequestDTO(
			pickupLocation    = PICKUP_LOCATION,
			dropOffLocation   = DROP_OFF_LOCATION,
			desiredTime       = DESIRED_TIME,
			isFemalePassenger = IS_FEMALE)

		// - - - Call the Search Service
		val results = SEARCH_SERVICE.searchRides(request)
		return ResponseEntity(results, HttpStatus.OK)
	}
}