package just.somebody.gigtern.security

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import just.somebody.gigtern.domain.repositories.UserRepository
import just.somebody.gigtern.utils.Logger
import org.springframework.http.HttpMethod
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
		val path = REQUEST.servletPath

		// 1. Check for Public Authentication Endpoints (Exact Match)
		val isAuthEndpoint = path == "/api/v1/register" || path == "/api/v1/login"

		// 2. Check for Public Gig Listing Endpoint (Exact Match + GET method)
		val isPublicGigListing = path == "/api/v1/gigs" && REQUEST.method == HttpMethod.GET.name()

		// Return true only if it is one of the strictly public endpoints
		return isAuthEndpoint || isPublicGigListing
	}

	private fun resolveToken(REQUEST: HttpServletRequest) : String?
	{
		val bearerToken = REQUEST.getHeader("Authorization")
		return if (bearerToken != null && bearerToken.startsWith("Bearer "))
		{
			Logger.LOG_INFO("[JWT Auth Filter] Extracted JWT: $bearerToken") // Log the extracted token
			bearerToken.substring(7)
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
				// - --  Validate token
				if (!PROVIDER.validateToken(jwt))
				{
					Logger.LOG_ERROR("[JWT Auth Filter] Invalid or expired JWT: $jwt")
					RESPONSE.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid or expired JWT")
					return
				}

				//  - -- Extract user ID (now expected to be a Long ID string)
				val userId = PROVIDER.getUserIdFromToken(jwt)
				if (userId == null)
				{
					Logger.LOG_ERROR("[JWT Auth Filter] Could not extract user ID from token")
					RESPONSE.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT payload (missing user ID)")
					return
				}

				// - - - Set Authentication Context if not already set
				if (SecurityContextHolder.getContext().authentication == null)
				{
					// - - - Fetch UserEntity from DB (ID is Long now)
					val userOptional = REPO.findById(userId)
					if (userOptional.isEmpty)
					{
						Logger.LOG_ERROR("[JWT Auth Filter] : User not found in database for ID: $userId")
						throw BadCredentialsException("User not found in database for ID: $userId")
					}

					// - - - Create authorities list using ROLE_ prefix
					val user        = userOptional.get()
					val authorities = listOf(SimpleGrantedAuthority("ROLE_${user.role.name}"))

					// - - - Create authentication token
					val auth = UsernamePasswordAuthenticationToken(
						user, // - - - @AUTH PRINCIPLA
						null,
						authorities)

					SecurityContextHolder.getContext().authentication = auth

					Logger.LOG_INFO("[JWT Auth Filter] Auth success for user ${user.id}")
				}
			}
			catch (e: Exception)
			{
				// - - - Catch all exceptions during processing (like BadCredentialsException from step 3)
				Logger.LOG_ERROR("[JWT Auth Filter] Error during authentication: ${e.message}")

				// - - - Send error response only if it hasn't been sent already
				if (!RESPONSE.isCommitted)
				{
					RESPONSE.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authentication failed: ${e.message}")
				}
				return
			}
		}

		// - - - Continue filter chain
		FILTER_CHAIN.doFilter(REQUEST, RESPONSE)
	}
}