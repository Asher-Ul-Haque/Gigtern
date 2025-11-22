package just.somebody.rideShareBackend.security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import just.somebody.rideShareBackend.domain.entities.UserEntity
import just.somebody.rideShareBackend.domain.enums.UserRole
import just.somebody.rideShareBackend.utils.Logger
import org.springframework.stereotype.Component
import java.util.Date
import javax.crypto.SecretKey

@Component
class JwtProvider(private val CONFIG: JwtConfig)
{
	// - - - private key
	private val key: SecretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(CONFIG.secret))

	// - - - generate a token
	fun generateToken(ROLL_NO: Int, ROLE: UserRole): String
	{
		val now         = Date()
		val expiryDate  = Date(now.time + CONFIG.expiration)

		return Jwts.builder()
			.subject(ROLL_NO.toString())
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

	// - - - extract the roll no
	fun getRollNoFromToken(JWT: String): String? = getClaimsFromToken(JWT)?.subject

	// - - - extract the role
	fun getRoleFromToken(JWT: String): String? = getClaimsFromToken(JWT)?.get("role") as String?

	// - - - validate token
	fun validateToken(JWT: String,) : Boolean
	{
		val claims    = getClaimsFromToken(JWT) ?: return false
		val isExpired = claims.expiration.before(Date())
		return !isExpired
	}
}