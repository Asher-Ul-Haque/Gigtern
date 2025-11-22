package just.somebody.rideShareBackend.security

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import just.somebody.rideShareBackend.domain.entities.UserEntity
import just.somebody.rideShareBackend.domain.repositories.UserRepository
import just.somebody.rideShareBackend.utils.Logger
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthFilter(
	private val PROVIDER: JwtProvider,
	private val REPO    : UserRepository): OncePerRequestFilter()
{
	override fun shouldNotFilter(REQUEST : HttpServletRequest): Boolean
	{
		val skip = listOf(
			"/api/v1/auth/",
			"/api/v1/rides/search",
			"/h2-console")
		val path = REQUEST.servletPath
		return skip.any { path.startsWith(it) }
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
		val jwt = resolveToken(REQUEST)

		if (jwt == null)
		{
			Logger.LOG_ERROR("[JWT Auth Filter] JWT missing in Authorization header")
			throw AuthenticationCredentialsNotFoundException("JWT token missing")
		}

		if (!PROVIDER.validateToken(jwt))
		{
			Logger.LOG_ERROR("[JWT Auth Filter] Invalid or expired JWT")
			throw BadCredentialsException("Invalid or expired JWT")
		}

		// - - - Extract roll number
		val rollNo = PROVIDER.getRollNoFromToken(jwt)
		if (rollNo == null)
		{
			Logger.LOG_ERROR("[JWT Auth Filter] Could not extract roll number from token")
			throw BadCredentialsException("Invalid JWT payload (missing roll number)")
		}

		// - - - Ensure context not already authenticated
		if (SecurityContextHolder.getContext().authentication == null)
		{
			val user: UserEntity = REPO.findById(rollNo.toInt())
				.orElseThrow()
				{
					Logger.LOG_ERROR("[JWT Auth Filter] User $rollNo not found in database")
					BadCredentialsException("User not found")
				}

			val authorities = listOf(SimpleGrantedAuthority("ROLE_${user.role.name}"))
			val auth = UsernamePasswordAuthenticationToken(
				user,
				null,
				authorities)
			SecurityContextHolder.getContext().authentication = auth

			Logger.LOG_INFO("[JWT Auth Filter] Auth success for user ${user.rollNo}")
		}

		FILTER_CHAIN.doFilter(REQUEST, RESPONSE)
	}
}