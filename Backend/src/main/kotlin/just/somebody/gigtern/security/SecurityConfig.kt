package just.somebody.gigtern.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
class SecurityConfig(
	private val JWT_CONFIG : JwtConfig,
	private val AUTH_FILER : JwtAuthFilter)
{
	@Bean
	fun securityFilterChain(HTTP: HttpSecurity): SecurityFilterChain
	{
		HTTP
			.csrf { it.disable() }
			.authorizeHttpRequests()
			{ auth ->
				auth
					.requestMatchers(*JWT_CONFIG.public.toTypedArray()).permitAll()
					.anyRequest().authenticated()
			}

			// - - - Configure session management to be stateless
			.sessionManagement { session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }

		// - - - Add the custom JWT filter before the standard authentication filter
		HTTP.addFilterBefore(AUTH_FILER, UsernamePasswordAuthenticationFilter::class.java)
		return HTTP.build()
	}

	@Bean
	fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()

	@Bean
	fun authenticationManager(CONFIG: AuthenticationConfiguration): AuthenticationManager = CONFIG.authenticationManager
}