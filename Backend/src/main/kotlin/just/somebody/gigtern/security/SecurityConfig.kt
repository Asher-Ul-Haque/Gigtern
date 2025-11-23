package just.somebody.gigtern.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.filter.CorsFilter // Import required filter
import java.util.Arrays

/**
 * Main Spring Security configuration class.
 * FIX: Manually placing CorsFilter before all other filters in the chain.
 */
@Configuration
@EnableWebSecurity
class SecurityConfig(private val AUTH_FILER: JwtAuthFilter)
{
	/**
	 * Defines the CORS configuration source used by Spring Security.
	 * Setting allowedOrigins to "*" for development testing on network IPs.
	 */
	@Bean
	fun corsConfigurationSource(): CorsConfigurationSource {
		val configuration = CorsConfiguration()

		// FIX: Allow all origins (for network testing)
		configuration.allowedOrigins = Arrays.asList("*")
		configuration.allowedMethods = Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
		configuration.allowedHeaders = Arrays.asList("*")

		// When using allowedOrigins("*"), allowCredentials MUST be false by browser rules.
		configuration.allowCredentials = false

		val source = UrlBasedCorsConfigurationSource()
		source.registerCorsConfiguration("/**", configuration)
		return source
	}

	/**
	 * Defines the security filter chain rules.
	 */
	@Bean
	fun securityFilterChain(HTTP: HttpSecurity, corsConfigurationSource: CorsConfigurationSource): SecurityFilterChain
	{
		HTTP
			// 1. MANUALLY ADD CorsFilter: This forces the CORS headers to be added immediately,
			// bypassing ordering issues with Spring Security.
			.addFilterBefore(CorsFilter(corsConfigurationSource), UsernamePasswordAuthenticationFilter::class.java)

			// 2. Disable CSRF for stateless REST APIs
			.csrf { it.disable() }

			// 3. Configure Authorization
			.authorizeHttpRequests()
			{ auth ->
				// OPTIONS requests are permitted by the CorsFilter, but we keep this line
				// as a defensive measure in the AuthorizationFilter as well.
				auth.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

				auth
					// FIX: Authentication endpoints are public
					.requestMatchers("/api/v1/auth/**").permitAll()
					// List Gigs endpoint is public (GET)
					.requestMatchers(HttpMethod.GET, "/api/v1/gigs").permitAll()
					// Protect all other necessary endpoints
					.requestMatchers("/api/v1/gigs/**").authenticated()
					.requestMatchers("/api/v1/users/**").authenticated()

					// All other requests require authentication
					.anyRequest().authenticated()
			}

			// 4. Configure session management to be stateless (essential for JWT)
			.sessionManagement { session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }

		// 5. Add the custom JWT filter before the standard authentication filter
		HTTP.addFilterBefore(AUTH_FILER, UsernamePasswordAuthenticationFilter::class.java)
		return HTTP.build()
	}

	/**
	 * Standard bean for password hashing/verification using BCrypt.
	 */
	@Bean
	fun passwordEncoder(): PasswordEncoder {
		return BCryptPasswordEncoder()
	}

	/**
	 * Required bean to handle the authentication process.
	 */
	@Bean
	fun authenticationManager(config: AuthenticationConfiguration): AuthenticationManager {
		return config.authenticationManager
	}
}