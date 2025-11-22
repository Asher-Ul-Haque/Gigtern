package just.somebody.rideShareBackend.security

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "jwt")
data class JwtConfig(
	val secret      : String,
	val expiration  : Long = 24 * 60 * 60 * 1000)
