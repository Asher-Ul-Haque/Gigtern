package just.somebody.gigtern

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class GigternApplication

fun main(ARGS: Array<String>)
{
	runApplication<GigternApplication>(*ARGS)
}
