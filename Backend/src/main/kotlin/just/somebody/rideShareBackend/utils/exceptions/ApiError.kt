package just.somebody.rideShareBackend.utils.exceptions

import org.springframework.http.HttpStatus

data class ApiError(
	val status    : HttpStatus,
	val error     : String,
	val message   : String?,
	val path      : String,
	val timestamp : Long = System.currentTimeMillis()
)
