package just.somebody.gigtern

import just.somebody.gigtern.security.JwtConfig
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties(JwtConfig::class)
class GigternApplication

fun main(ARGS: Array<String>)
{
	runApplication<GigternApplication>(*ARGS)
}
