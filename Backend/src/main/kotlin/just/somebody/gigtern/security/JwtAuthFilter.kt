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
import java.util.UUID


@Component
class JwtAuthFilter(
	private val PROVIDER : JwtProvider,
	private val REPO     : UserRepository,
	private val CONFIG   : JwtConfig
): OncePerRequestFilter()
{
	override fun shouldNotFilter(REQUEST : HttpServletRequest): Boolean
	{
		val path = REQUEST.servletPath
		return CONFIG.public.any { path.startsWith(it) }
	}

	private fun resolveToken(REQUEST: HttpServletRequest) : String?
	{
		val bearerToken = REQUEST.getHeader("Authorization")
		return if (bearerToken != null && bearerToken.startsWith("Bearer "))
		{ bearerToken.substring(7) }
		else null
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
			// - - - Validate token
			if (!PROVIDER.validateToken(jwt))
			{
				Logger.LOG_ERROR("[JWT Auth Filter] Invalid or expired JWT: $jwt")
				RESPONSE.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid or expired JWT")
				return
			}

			// - - - Extract user ID (UUID string)
			val userIdString = PROVIDER.getUserIdFromToken(jwt)
			if (userIdString == null)
			{
				Logger.LOG_ERROR("[JWT Auth Filter] Could not extract user ID from token")
				RESPONSE.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT payload")
				return
			}

			// - - - Set Authentication Context if not already set
			if (SecurityContextHolder.getContext().authentication == null)
			{
				try
				{
					val userId = UUID.fromString(userIdString)
					val user   = REPO.findById(userId)
					if (user.isEmpty)
					{
						Logger.LOG_ERROR("[JWT Auth Filter] : User not found in database for ID: $userId")
						throw BadCredentialsException("User not found in database for ID: $userIdString")
					}

					// - - - Create authorities list using ROLE_ prefix
					val authorities = listOf(SimpleGrantedAuthority("ROLE_${user.get().name}"))

					// - - - Create authentication token
					val auth = UsernamePasswordAuthenticationToken(
						user,
						null,
						authorities)

					SecurityContextHolder.getContext().authentication = auth

					Logger.LOG_INFO("[JWT Auth Filter] Auth success for user ${user.get().id}")
				}
				catch (e: Exception)
				{
					Logger.LOG_ERROR("[JWT Auth Filter] Error during authentication: ${e.message}")
					RESPONSE.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authentication failed")
					return
				}
			}
		}

		// - - - Continue filter chain
		FILTER_CHAIN.doFilter(REQUEST, RESPONSE)
	}
}