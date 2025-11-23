package just.somebody.gigtern.utils.exceptions

import just.somebody.gigtern.utils.Logger
import jakarta.servlet.http.HttpServletRequest
import jakarta.validation.ValidationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.security.core.AuthenticationException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.lang.SecurityException

@RestControllerAdvice
class GlobalExceptionHandler
{
	// - - - Handle ApplicationException -> 400 Bad Request (Due to invalid state, e.g., already applied, gig closed)
	@ExceptionHandler(ApplicationException::class)
	fun handleApplicationException(
		EXCEPTION : ApplicationException,
		REQUEST   : HttpServletRequest
	): ResponseEntity<ApiError> = handleError(EXCEPTION, REQUEST, HttpStatus.BAD_REQUEST)

	// - - - Handle ResourceNotFoundException -> 404 Not Found
	@ExceptionHandler(ResourceNotFoundException::class)
	fun handleResourceNotFound(
		EXCEPTION : ResourceNotFoundException,
		REQUEST   : HttpServletRequest
	): ResponseEntity<ApiError> = handleError(EXCEPTION, REQUEST, HttpStatus.NOT_FOUND)

	// - - - Handle AuthorizationException -> 403 Forbidden (Unauthorized role/ownership)
	@ExceptionHandler(AuthorizationException::class)
	fun handleAuthorizationException(
		EXCEPTION : AuthorizationException,
		REQUEST   : HttpServletRequest
	): ResponseEntity<ApiError> = handleError(EXCEPTION, REQUEST, HttpStatus.FORBIDDEN)

	// - - - Handle UserAlreadyExistsException -> 409 Conflict
	@ExceptionHandler(UserAlreadyExistsException::class)
	fun handleUserAlreadyExists(
		EXCEPTION : UserAlreadyExistsException,
		REQUEST   : HttpServletRequest
	): ResponseEntity<ApiError> = handleError(EXCEPTION, REQUEST, HttpStatus.CONFLICT)

	// - - - Handle UserNotFoundException -> 404 Not Found
	@ExceptionHandler(UserNotFoundException::class)
	fun handleUserNotFound(
		EXCEPTION : UserNotFoundException,
		REQUEST   : HttpServletRequest
	): ResponseEntity<ApiError> = handleError(EXCEPTION, REQUEST, HttpStatus.NOT_FOUND)

	// - - - Handle SecurityException/AuthenticationException -> 401 Unauthorized
	@ExceptionHandler(SecurityException::class, AuthenticationException::class)
	fun handleAuthenticationFailure(
		EXCEPTION : Exception,
		REQUEST   : HttpServletRequest
	): ResponseEntity<ApiError> = handleError(EXCEPTION, REQUEST, HttpStatus.UNAUTHORIZED)

	// - - - Handle Bad Requests
	@ExceptionHandler(IllegalArgumentException::class)
	fun handleBadRequest(
		EXCEPTION : IllegalArgumentException,
		REQUEST   : HttpServletRequest
	): ResponseEntity<ApiError> = handleError(EXCEPTION, REQUEST, HttpStatus.BAD_REQUEST)

	// - - - Handle Illegal Exceptions
	@ExceptionHandler(IllegalStateException::class)
	fun handleForbidden(
		EXCEPTION : IllegalStateException,
		REQUEST   : HttpServletRequest
	): ResponseEntity<ApiError> = handleError(EXCEPTION, REQUEST, HttpStatus.FORBIDDEN)

	// - - - Handle Validation Errors
	@ExceptionHandler(MethodArgumentNotValidException::class, ValidationException::class)
	fun handleValidation(
		EXCEPTION : Exception,
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
			error   = EXCEPTION::class.simpleName ?: "Error",
			message = EXCEPTION.message,
			path    = REQUEST.requestURI)

		return ResponseEntity(error, STATUS)
	}

	fun handleError(
		EXCEPTION : HttpMessageNotReadableException,
		REQUEST   : HttpServletRequest,
		STATUS    : HttpStatus = HttpStatus.INTERNAL_SERVER_ERROR
	): ResponseEntity<ApiError>
	{
		Logger.LOG_ERROR("[Global Exception Handler] : ${STATUS.value()} - ${EXCEPTION.message}")

		val error = ApiError(
			status  = STATUS,
			error   = EXCEPTION::class.simpleName ?: "Error",
			message = EXCEPTION.message,
			path    = REQUEST.requestURI)

		return ResponseEntity(error, STATUS)
	}
}

// - - - TODO: HttpMessageNotReadableException