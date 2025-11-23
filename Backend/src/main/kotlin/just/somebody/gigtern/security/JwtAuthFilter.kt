package just.somebody.gigtern.security

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import just.somebody.gigtern.domain.entities.UserEntity
import just.somebody.gigtern.domain.repositories.UserRepository
import just.somebody.gigtern.utils.Logger
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthFilter(
	private val PROVIDER : JwtProvider,
	private val REPO     : UserRepository,
	private val CONFIG   : JwtConfig
): OncePerRequestFilter()
{
	override fun shouldNotFilter(REQUEST : HttpServletRequest): Boolean
	{
		Logger.LOG_INFO(REQUEST.servletPath)
		return false
	}


	private fun resolveToken(REQUEST: HttpServletRequest) : String?
	{
		val bearerToken = REQUEST.getHeader("Authorization")

		if (bearerToken.isNullOrEmpty()) {
			return null
		}

		return if (bearerToken.startsWith("Bearer "))
		{
			val token = bearerToken.substring(7)
			// Logger.LOG_INFO("[JWT Auth Filter] Extracted JWT: $token") // Sensitive logging removed
			token
		}
		else
		{
			Logger.LOG_ERROR("[JWT Auth Filter] Authorization header found but doesn't start with 'Bearer '.")
			null
		}
	}

	override fun doFilterInternal(
		REQUEST       : HttpServletRequest,
		RESPONSE      : HttpServletResponse,
		FILTER_CHAIN  : FilterChain)
	{
		// - - - Resolve token
		val jwt = resolveToken(REQUEST)
		if (jwt != null)
		{
			try
			{
				// 1. Validate token
				if (!PROVIDER.validateToken(jwt))
				{
					Logger.LOG_ERROR("[JWT Auth Filter] Invalid or expired JWT.")
					RESPONSE.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid or expired JWT")
					return
				}

				// 2. Extract user ID and validate
				val userId= PROVIDER.getUserIdFromToken(jwt)


				if (userId == null) {
					Logger.LOG_ERROR("[JWT Auth Filter] Could not extract valid Long user ID from token.")
					RESPONSE.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT payload (ID format)")
					return
				}

				// 3. Set Authentication Context
				if (SecurityContextHolder.getContext().authentication == null)
				{
					val userOptional = REPO.findById(userId)

					if (userOptional.isEmpty)
					{
						Logger.LOG_ERROR("[JWT Auth Filter] : User not found in database for ID: $userId")
						RESPONSE.sendError(HttpServletResponse.SC_UNAUTHORIZED, "User not found in database.")
						return
					}

					val user        = userOptional.get()
					val authorities = listOf(SimpleGrantedAuthority("ROLE_${user.role.name}"))
					val auth = UsernamePasswordAuthenticationToken(user, null, authorities)

					SecurityContextHolder.getContext().authentication = auth

					Logger.LOG_INFO("[JWT Auth Filter] Auth success for user ${user.id} (${user.role.name})")
				}
			}
			catch (e: Exception)
			{
				Logger.LOG_ERROR("[JWT Auth Filter] Error during authentication: ${e.message}")
				if (!RESPONSE.isCommitted)
				{
					RESPONSE.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authentication failed: ${e.message}")
				}
				return
			}
		}

		// Continue filter chain. SecurityConfig's AuthorizationFilter will determine permission.
		FILTER_CHAIN.doFilter(REQUEST, RESPONSE)
	}
}