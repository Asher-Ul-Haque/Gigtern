package just.somebody.gigtern.security

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@ConfigurationProperties(prefix = "jwt")
data class JwtConfig(
	val secret      : String,
	val issuer      : String,
	val receiver    : String,
	val expiration  : Long          = 24 * 60 * 60 * 1000,
	val public      : List<String>  = emptyList())

@Configuration
class SecurityConfig(
	private val JWT_FILTER : JwtFilter,
	private val JWT_CONFIG : JwtConfig,
	private val ENTRY_POINT: AuthenticationEntryPoint)
{
	@Bean
	fun filterChain(HTTP: HttpSecurity) : SecurityFilterChain
	{
		return HTTP
			.csrf { it.disable() }
			.sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
			.exceptionHandling { it.authenticationEntryPoint(ENTRY_POINT) }
			.authorizeHttpRequests()
			{ request ->
				JWT_CONFIG.public.forEach { endpoint -> request.requestMatchers(endpoint).permitAll() }
				request.anyRequest().authenticated()
			}
			.addFilterBefore(JWT_FILTER, UsernamePasswordAuthenticationFilter::class.java)
			.build()
	}

	@Bean
	fun passwordEncoder() : PasswordEncoder = BCryptPasswordEncoder()
}