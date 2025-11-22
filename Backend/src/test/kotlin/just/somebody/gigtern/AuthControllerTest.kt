package just.somebody.gigtern

import io.restassured.RestAssured
import io.restassured.RestAssured.given
import org.hamcrest.CoreMatchers.equalTo
import org.junit.jupiter.api.BeforeEach
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import kotlin.test.Test

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuthControllerRestAssuredTest(@LocalServerPort val port: Int)
{

	@BeforeEach
	fun setup() {
		RestAssured.baseURI = "http://localhost"
		RestAssured.port = port
	}

	@Test
	fun `signup works like a real REST call`() {
		given()
			.contentType("application/json")
			.header("X-Forwarded-For", "10.0.0.1")
			.body(""" { "rollNo": 1, "name": "John" } """)
			.`when`()
			.post("/api/v1/auth/signup")
			.then()
			.statusCode(201)
			.body("rollNo", equalTo(1))
			.body("name", equalTo("John"))
	}
}
