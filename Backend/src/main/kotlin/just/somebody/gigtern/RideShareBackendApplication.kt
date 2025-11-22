package just.somebody.gigtern

import just.somebody.gigtern.security.JwtConfig
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.web.client.RestTemplate

@EnableConfigurationProperties(JwtConfig::class)
@SpringBootApplication

class RideShareBackendApplication
{
	@Bean
	fun restTemplate(): RestTemplate {
		// Simple RestTemplate used for the mock network verification call
		return RestTemplate()
	}
}

fun main(args: Array<String>) {
	runApplication<RideShareBackendApplication>(*args)
}
