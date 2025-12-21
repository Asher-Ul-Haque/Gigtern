package just.somebody.gigtern.security

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerExceptionResolver

@Component
class AuthenticationEntryPoint(
	@Qualifier("handlerExceptionResolver")
	private val RESOLVER: HandlerExceptionResolver
) : AuthenticationEntryPoint
{
	override fun commence(
		REQUEST   : HttpServletRequest,
		RESPONSE  : HttpServletResponse,
		EXCEPTION : AuthenticationException)
	{
		// - - - send to global exception handler
		RESOLVER.resolveException(REQUEST, RESPONSE, null, EXCEPTION)
	}
}