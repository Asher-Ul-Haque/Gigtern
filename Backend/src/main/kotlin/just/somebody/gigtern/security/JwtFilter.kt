package just.somebody.gigtern.security

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import just.somebody.gigtern.service.UserDetailsService
import just.somebody.gigtern.utils.Logger
import just.somebody.gigtern.utils.exceptions.UserNotFoundException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.util.AntPathMatcher
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtFilter(
	private val PROVIDER    : JwtProvider,
	private val USER_SERVICE: UserDetailsService,
	private val JWT_CONFIG  : JwtConfig
) : OncePerRequestFilter()
{
	override fun shouldNotFilter(REQUEST: HttpServletRequest): Boolean
	{
		val ip = REQUEST.getHeader("X-Forwarded-For")
			?.split(",")
			?.firstOrNull()
			?.trim()
			?: REQUEST.remoteAddr

		val toBlock = JWT_CONFIG.public.any { endpoint -> pathMatcher(endpoint, REQUEST.servletPath) }
		Logger.LOG_INFO("[Jwt Filter] Got a request at ${REQUEST.servletPath} from $ip, blocking status $toBlock")
		return toBlock
	}

	private fun pathMatcher(PATTERN: String, PATH: String): Boolean = AntPathMatcher().match(PATTERN, PATH)

	private fun resolveToken(REQUEST: HttpServletRequest): String?
	{
		val bearerToken = REQUEST.getHeader("Authorization")
		if (bearerToken.isNullOrEmpty()) return null

		val bearer = "Bearer "
		return if (bearerToken.startsWith(bearer))
		{
			bearerToken.substring(bearer.length)
		}
		else
		{
			Logger.LOG_ERROR("[Jwt Filter] Authorization header found but doesnt start with 'Bearer '.")
			null
		}
	}


	override fun doFilterInternal(
		REQUEST     : HttpServletRequest,
		RESPONSE    : HttpServletResponse,
		FILTER_CHAIN: FilterChain
	)
	{
		// - - - resolve token
		val jwt = resolveToken(REQUEST)
		if (jwt != null && SecurityContextHolder.getContext().authentication == null)
		{
			val claims = PROVIDER.validateAndGetClaims(jwt)
			if (claims != null)
			{
				val userId    = claims.subject.toLong()
				val role      = claims.get("role", String::class.java)
				val user      = USER_SERVICE.loadUserById(userId) ?: throw UserNotFoundException("User not found")
				val powers    = listOf(SimpleGrantedAuthority("ROLE_$role"))
				val auth      = UsernamePasswordAuthenticationToken(user, null, powers)
				auth.details  = WebAuthenticationDetailsSource().buildDetails(REQUEST)

				SecurityContextHolder.getContext().authentication  = auth
			}
		}
		FILTER_CHAIN.doFilter(REQUEST, RESPONSE)
	}
}