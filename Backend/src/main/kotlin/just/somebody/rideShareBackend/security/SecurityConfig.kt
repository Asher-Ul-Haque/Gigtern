package just.somebody.rideShareBackend.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
class SecurityConfig(private val AUTH_FILER: JwtAuthFilter)
{
	@Bean
	fun securityFilterChain(HTTP: HttpSecurity): SecurityFilterChain
	{
		HTTP
			.csrf { it.disable() }
			.authorizeHttpRequests()
			{ auth ->
				auth
					.requestMatchers("/api/v1/auth/**").permitAll()       // - - - auth endpoints are open
					.requestMatchers("/api/v1/rides/search").permitAll()  // - - - public read for search access
					.requestMatchers("/h2-console/**").permitAll()        // - - - allow H2 console
					.anyRequest().authenticated()                                      // - - - anything else is authenticated
			}
			.sessionManagement { session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
			.headers { headers -> headers.frameOptions { it.disable() } }

		HTTP.addFilterBefore(AUTH_FILER, UsernamePasswordAuthenticationFilter::class.java)
		return HTTP.build()
	}
}