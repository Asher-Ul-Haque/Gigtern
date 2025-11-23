package just.somebody.gigtern.security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import just.somebody.gigtern.domain.entities.UserEntity
import just.somebody.gigtern.domain.enums.Role
import just.somebody.gigtern.utils.Logger
import just.somebody.gigtern.utils.exceptions.AuthorizationException
import org.springframework.context.annotation.Bean
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import java.util.Date
import javax.crypto.SecretKey


@Component
class JwtProvider(private val CONFIG: JwtConfig)
{
	// - - - private key
	private val key: SecretKey by lazy {  Keys.hmacShaKeyFor(Decoders.BASE64.decode(CONFIG.secret)) }

	// - - - generate a token
	fun generateToken(USER_ID: Long, ROLE: Role): String
	{
		val now         = Date()
		val expiryDate  = Date(now.time + CONFIG.expiration)

		return Jwts.builder()
			.subject(USER_ID.toString())
			.claim("role", ROLE.name)
			.issuedAt(now)
			.expiration(expiryDate)
			.signWith(key)
			.compact()
	}

	// - - - get claims from token
	fun getClaimsFromToken(JWT: String): Claims?
	{
		return try
		{
			Jwts.parser()
				.verifyWith(key)
				.build()
				.parseSignedClaims(JWT)
				.payload
		}
		catch (e : Exception)
		{
			Logger.LOG_ERROR("[JWT Provider] : JWT validation failed: ${e.message}")
			null
		}
	}

	// - - - extract the user ID
	fun getUserIdFromToken(JWT: String): Long? = getClaimsFromToken(JWT)?.subject?.toLong()

	// - - - validate token
	fun validateToken(JWT: String) : Boolean
	{
		val claims    = getClaimsFromToken(JWT) ?: return false
		val isExpired = claims.expiration.before(Date())
		return !isExpired
	}
}
