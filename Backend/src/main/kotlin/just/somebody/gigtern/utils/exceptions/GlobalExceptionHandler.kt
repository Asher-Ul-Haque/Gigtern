package just.somebody.gigtern.utils.exceptions

import jakarta.servlet.http.HttpServletRequest
import just.somebody.gigtern.service.exceptions.UserAlreadyExistsException
import just.somebody.gigtern.service.exceptions.UserNotFoundException
import just.somebody.gigtern.utils.Logger
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.AuthenticationException
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

	@ExceptionHandler(UserAlreadyExistsException::class)
	fun handleUserAlreadyExists(
		EXCEPTION : UserAlreadyExistsException,
		REQUEST   : HttpServletRequest
	): ResponseEntity<ApiError> = handleError(EXCEPTION, REQUEST, HttpStatus.CONFLICT) // 409

	@ExceptionHandler(UserNotFoundException::class)
	fun handleUserNotFound(
		EXCEPTION : UserNotFoundException,
		REQUEST   : HttpServletRequest
	): ResponseEntity<ApiError> = handleError(EXCEPTION, REQUEST, HttpStatus.NOT_FOUND) // 404

	@ExceptionHandler(SecurityException::class, AuthenticationException::class)
	fun handleAuthenticationFailure(
		EXCEPTION : Exception,
		REQUEST   : HttpServletRequest
	): ResponseEntity<ApiError> = handleError(EXCEPTION, REQUEST, HttpStatus.UNAUTHORIZED) // 401

	@ExceptionHandler(AuthorizationException::class)
	fun handleAuthorizationException(
		EXCEPTION : AuthorizationException,
		REQUEST   : HttpServletRequest
	): ResponseEntity<ApiError> = handleError(EXCEPTION, REQUEST, HttpStatus.FORBIDDEN) // 403
}