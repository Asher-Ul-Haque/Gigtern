package just.somebody.gigtern.security

import just.somebody.gigtern.domain.entities.UserEntity
import just.somebody.gigtern.utils.exceptions.AuthorizationException
import org.springframework.security.core.context.SecurityContextHolder

object SecurityUtils
{
	fun getAuthenticatedUser(): UserEntity
	{
		val authentication = SecurityContextHolder.getContext().authentication

		if (authentication == null                      ||
				authentication.principal == "anonymousUser" ||
				authentication.principal !is UserEntity)
		{
			// - - - This exception will be caught by the GlobalExceptionHandler (401/403)
			throw AuthorizationException("Authentication token is missing or invalid.")
		}

		return authentication.principal as UserEntity
	}
}