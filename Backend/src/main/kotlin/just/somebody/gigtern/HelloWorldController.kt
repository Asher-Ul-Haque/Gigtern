package just.somebody.gigtern


import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HelloController {

	@GetMapping("/")
	fun index(): String = "Hello World!"
}
