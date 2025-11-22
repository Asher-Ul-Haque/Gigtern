package just.somebody.gigtern.security

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "jwt")
data class JwtConfig(
	val secret      : String,
	val expiration  : Long          = 24 * 60 * 60 * 1000,
	val public      : List<String>  = emptyList())
