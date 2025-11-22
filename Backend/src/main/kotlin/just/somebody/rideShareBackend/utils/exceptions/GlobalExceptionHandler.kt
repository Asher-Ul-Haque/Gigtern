package just.somebody.rideShareBackend.utils.exceptions

import jakarta.servlet.http.HttpServletRequest
import just.somebody.rideShareBackend.utils.Logger
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler
{
	@ExceptionHandler(IllegalArgumentException::class)
	fun handleBadRequest(
		EXCEPTION : IllegalArgumentException,
		REQUEST   : HttpServletRequest
	): ResponseEntity<ApiError> = handleError(EXCEPTION, REQUEST, HttpStatus.BAD_REQUEST)

	@ExceptionHandler(IllegalStateException::class)
	fun handleForbidden(
		EXCEPTION : IllegalStateException,
		REQUEST   : HttpServletRequest
	): ResponseEntity<ApiError> = handleError(EXCEPTION, REQUEST, HttpStatus.FORBIDDEN)

	@ExceptionHandler(MethodArgumentNotValidException::class)
	fun handleValidation(
		EXCEPTION : MethodArgumentNotValidException,
		REQUEST   : HttpServletRequest
	): ResponseEntity<ApiError> = handleError(EXCEPTION, REQUEST, HttpStatus.BAD_REQUEST)

	fun handleError(
		EXCEPTION : Exception,
		REQUEST   : HttpServletRequest,
		STATUS    : HttpStatus = HttpStatus.INTERNAL_SERVER_ERROR
	): ResponseEntity<ApiError>
	{
		Logger.LOG_ERROR("[Global Exception Handler] : ${STATUS.value()} - ${EXCEPTION.message}")

		val error = ApiError(
			status  = STATUS,
			error   = EXCEPTION::class.toString(),
			message = EXCEPTION.message,
			path    = REQUEST.requestURI)

		return ResponseEntity(error, STATUS)
	}
}