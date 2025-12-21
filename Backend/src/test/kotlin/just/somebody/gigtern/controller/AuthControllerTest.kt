package just.somebody.gigtern.controller

import just.somebody.gigtern.controller.dtos.requests.LoginRequest
import just.somebody.gigtern.controller.dtos.requests.RegisterRequest
import just.somebody.gigtern.controller.dtos.response.TokenResponse
import just.somebody.gigtern.security.SecurityConfig
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.web.servlet.client.RestTestClient
import org.springframework.web.context.WebApplicationContext
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity
import org.springframework.test.web.servlet.setup.ConfigurableMockMvcBuilder

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@Import(SecurityConfig::class)
class AuthControllerTest(private val WAC: WebApplicationContext)
{
	private lateinit var restTestClient: RestTestClient

	@BeforeEach
	fun setup()
	{
		// - - - WARNING: see https://github.com/spring-projects/spring-framework/issues/35881
		restTestClient = RestTestClient
			.bindToApplicationContext(WAC)
			.configureServer<RestTestClient.WebAppContextSetupBuilder>()
			{ builder -> (builder as ConfigurableMockMvcBuilder<*>).apply(springSecurity()) }
			.build()
	}

	@Test
	fun `register works`()
	{
		restTestClient.post()
			.uri("/api/v1/auth/register")
			.body(RegisterRequest("John Doe", "john1@test.com", "StrongPass123!"))
			.exchange()
			.expectStatus().isCreated
	}

	@Test
	fun `cant register twice`()
	{
		val req = RegisterRequest("John Doe", "john2@test.com", "StrongPass123!")
		restTestClient.post().uri("/api/v1/auth/register").body(req).exchange()
		restTestClient.post().uri("/api/v1/auth/register").body(req).exchange()
			.expectStatus().is4xxClientError
	}

	@Test
	fun `cant register with weak password`()
	{
		restTestClient.post()
			.uri("/api/v1/auth/register")
			.body(RegisterRequest("John Doe", "john3@test.com", "123"))
			.exchange()
			.expectStatus().isBadRequest
	}

	@Test
	fun `cant register with weak name`()
	{
		restTestClient.post()
			.uri("/api/v1/auth/register")
			.body(RegisterRequest("A", "john4@test.com", "StrongPass123!"))
			.exchange()
			.expectStatus().isBadRequest
	}

	@Test
	fun `cant register with weak email`()
	{
		restTestClient.post()
			.uri("/api/v1/auth/register")
			.body(RegisterRequest("John Doe", "bad", "StrongPass123!"))
			.exchange()
			.expectStatus().isBadRequest
	}

	@Test
	fun `login works`()
	{
		restTestClient.post()
			.uri("/api/v1/auth/register")
			.body(RegisterRequest("John Doe", "john5@test.com", "StrongPass123!"))
			.exchange()

		restTestClient.post()
			.uri("/api/v1/auth/login")
			.body(LoginRequest("john5@test.com", "StrongPass123!"))
			.exchange()
			.expectStatus().isAccepted
	}

	@Test
	fun `cant login with wrong password`()
	{
		restTestClient.post()
			.uri("/api/v1/auth/register")
			.body(RegisterRequest("John Doe", "john6@test.com", "StrongPass123!"))
			.exchange()

		restTestClient.post()
			.uri("/api/v1/auth/login")
			.body(LoginRequest("john6@test.com", "WrongPass"))
			.exchange()
			.expectStatus().isUnauthorized
	}

	@Test
	fun `cant login with weak password`()
	{
		restTestClient.post()
			.uri("/api/v1/auth/login")
			.body(LoginRequest("john@test.com", "123"))
			.exchange()
			.expectStatus().isBadRequest
	}

	@Test
	fun `cant login with weak email`()
	{
		restTestClient.post()
			.uri("/api/v1/auth/login")
			.body(LoginRequest("bad", "StrongPass123!"))
			.exchange()
			.expectStatus().isBadRequest
	}

	@Test
	fun `hello works publicly`()
	{
		restTestClient.get()
			.uri("/api/v1/auth/hello")
			.exchange()
			.expectStatus().isOk
	}

	@Test
	fun `hi does not work without JWT`()
	{
		restTestClient.get()
			.uri("/api/v1/auth/hi")
			.exchange()
			.expectStatus().isUnauthorized
	}

	@Test
	fun `hi works with JWT`()
	{
		val token = restTestClient.post()
			.uri("/api/v1/auth/register")
			.body(RegisterRequest("John Doe", "john7@test.com", "StrongPass123!"))
			.exchange()
			.expectBody(TokenResponse::class.java)
			.returnResult().responseBody!!.accessToken

		restTestClient.get()
			.uri("/api/v1/auth/hi")
			.header("Authorization", "Bearer $token")
			.exchange()
			.expectStatus().isOk
	}
}