package just.somebody.gigtern.security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import just.somebody.gigtern.domain.enums.Role
import just.somebody.gigtern.utils.Logger
import org.springframework.stereotype.Component
import java.util.Date
import javax.crypto.SecretKey

@Component
class JwtProvider(private val CONFIG : JwtConfig)
{
	// - - - private key
	private val key: SecretKey
	init
	{
		val decoded = Decoders.BASE64.decode(CONFIG.secret)
		require(decoded.size >= 32) { "JWT secret must be at least 256 bits "}
		key = Keys.hmacShaKeyFor(decoded)
	}

	private val parser = Jwts
		.parser()
		.verifyWith(key)
		.requireIssuer(CONFIG.issuer)
		.requireAudience(CONFIG.receiver)
		.build()

	fun validateAndGetClaims(TOKEN: String): Claims?
	{
		return try            { parser.parseSignedClaims(TOKEN).payload }
		catch (e: Exception)  { null }
	}

	// - - - generate a token
	fun generateToken(USER_ID: Long, ROLE: Role): String
	{
		val now         = Date()
		val expiryDate  = Date(now.time + CONFIG.expiration)

		return Jwts.builder()
			.issuer(CONFIG.issuer)
			.audience().add(CONFIG.receiver).and()
			.subject(USER_ID.toString())
			.claim("role", ROLE.name)
			.issuedAt(now)
			.expiration(expiryDate)
			.signWith(key)
			.compact()
	}
}