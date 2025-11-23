package just.somebody.gigtern


import just.somebody.gigtern.controllers.dtos.requests.AuthRequestDTO
import just.somebody.gigtern.domain.entities.EmployerEntity
import just.somebody.gigtern.domain.entities.StudentEntity
import just.somebody.gigtern.domain.entities.UserEntity
import just.somebody.gigtern.domain.enums.Role
import just.somebody.gigtern.domain.repositories.EmployerRepository
import just.somebody.gigtern.domain.repositories.StudentRepository
import just.somebody.gigtern.domain.repositories.UserRepository
import just.somebody.gigtern.security.JwtProvider
import just.somebody.gigtern.service.AuthService
import just.somebody.gigtern.utils.exceptions.UserAlreadyExistsException
import just.somebody.gigtern.utils.exceptions.UserNotFoundException
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.security.crypto.password.PasswordEncoder

@ExtendWith(MockitoExtension::class)
class AuthServiceTest
{
	@Mock private lateinit var USER_REPO      : UserRepository
	@Mock private lateinit var EMPLOYER_REPO  : EmployerRepository
	@Mock private lateinit var STUDENT_REPO   : StudentRepository
	@Mock private lateinit var JWT_PROVIDER   : JwtProvider
	@Mock private lateinit var ENCODER        : PasswordEncoder

	private lateinit var AUTH_SERVICE: AuthService

	// - - - Mock Data
	private val MOCK_EMPLOYER_USER = UserEntity(
		id            = 1L,
		name          = "Boss Man",
		email         = "employer@gig.com",
		passwordHash  = "hashed_pass",
		role          = Role.EMPLOYER)
	private val MOCK_STUDENT_USER = UserEntity(
		id            = 2L,
		name          = "Jane Doe",
		email         = "student@gig.com",
		passwordHash  = "hashed_pass",
		role = Role.STUDENT)
	private val MOCK_EMPLOYER_PROFILE = EmployerEntity(
		user            = MOCK_EMPLOYER_USER,
		pointOfContact  = "Boss Man",
		aadharKycUrl    = null)
	private val MOCK_STUDENT_PROFILE = StudentEntity(user = MOCK_STUDENT_USER)

	@BeforeEach
	fun setUp()
	{
		AUTH_SERVICE = AuthService(
			USER_REPO,
			EMPLOYER_REPO,
			STUDENT_REPO,
			JWT_PROVIDER,
			ENCODER)
	}


	// - - - Register User Tests - - -

	@Test
	fun `registerUser should successfully register a new employer and create profile`()
	{
		val request = AuthRequestDTO("Boss Man", "employer@gig.com", "pass123", Role.EMPLOYER)

		// - --  Mock repository calls
		`when`(USER_REPO.findByEmail(request.email))
			.thenReturn(null)
		`when`(ENCODER.encode(request.password))
			.thenReturn("hashed_pass")
		`when`(USER_REPO.save(any(UserEntity::class.java)))
			.thenReturn(MOCK_EMPLOYER_USER)
		`when`(EMPLOYER_REPO.save(any(EmployerEntity::class.java)))
			.thenReturn(MOCK_EMPLOYER_PROFILE)
		`when`(JWT_PROVIDER.generateToken(MOCK_EMPLOYER_USER.id, MOCK_EMPLOYER_USER.role))
			.thenReturn("mock.jwt.token")

		val response = AUTH_SERVICE.registerUser(request)

		// - - - Assertions
		assertEquals("mock.jwt.token", response.jwt)
		assertEquals(Role.EMPLOYER, response.role)
		verify(USER_REPO, times(1)).save(any(UserEntity::class.java))
		verify(EMPLOYER_REPO, times(1)).save(any(EmployerEntity::class.java))
		verify(STUDENT_REPO, never()).save(any(StudentEntity::class.java))
	}

	@Test
	fun `registerUser should successfully register a new student and create profile`()
	{
		val request = AuthRequestDTO("Jane Doe", "student@gig.com", "pass123", Role.STUDENT)

		// - - - Mock repository calls
		`when`(USER_REPO.findByEmail(request.email))
			.thenReturn(null)
		`when`(ENCODER.encode(request.password))
			.thenReturn("hashed_pass")
		`when`(USER_REPO.save(any(UserEntity::class.java)))
			.thenReturn(MOCK_STUDENT_USER)
		`when`(STUDENT_REPO.save(any(StudentEntity::class.java)))
			.thenReturn(MOCK_STUDENT_PROFILE)
		`when`(JWT_PROVIDER.generateToken(MOCK_STUDENT_USER.id, MOCK_STUDENT_USER.role))
			.thenReturn("mock.jwt.token")

		val response = AUTH_SERVICE.registerUser(request)

		// - - - Assertions
		assertEquals(Role.STUDENT, response.role)
		verify(USER_REPO, times(1)).save(any(UserEntity::class.java))
		verify(STUDENT_REPO, times(1)).save(any(StudentEntity::class.java))
		verify(EMPLOYER_REPO, never()).save(any(EmployerEntity::class.java))
	}

	@Test
	fun `registerUser should throw UserAlreadyExistsException for duplicate email`()
	{
		val request = AuthRequestDTO("Boss Man", "employer@gig.com", "pass123", Role.EMPLOYER)
		`when`(USER_REPO.findByEmail(request.email))
			.thenReturn(MOCK_EMPLOYER_USER)

		assertThrows<UserAlreadyExistsException> { AUTH_SERVICE.registerUser(request) }
		verify(ENCODER, never()).encode(anyString())
	}


	// - - - Login User Tests - - -

	@Test
	fun `loginUser should return JWT on successful login`()
	{
		val request = AuthRequestDTO("Boss Man", "employer@gig.com", "pass123", Role.EMPLOYER)

		`when`(USER_REPO.findByEmail(request.email))
			.thenReturn(MOCK_EMPLOYER_USER)
		`when`(ENCODER.matches(request.password, MOCK_EMPLOYER_USER.passwordHash))
			.thenReturn(true)
		`when`(JWT_PROVIDER.generateToken(MOCK_EMPLOYER_USER.id, MOCK_EMPLOYER_USER.role))
			.thenReturn("valid.jwt.token")

		val response = AUTH_SERVICE.loginUser(request)

		assertEquals("valid.jwt.token", response.jwt)
	}

	@Test
	fun `loginUser should throw UserNotFoundException if email not found`() {
		val request = AuthRequestDTO("Unknown", "unknown@gig.com", "pass123", Role.EMPLOYER)

		`when`(USER_REPO.findByEmail(request.email))
			.thenReturn(null)

		assertThrows<UserNotFoundException> { AUTH_SERVICE.loginUser(request) }
	}

	@Test
	fun `loginUser should throw SecurityException for incorrect password`()
	{
		val request = AuthRequestDTO("Boss Man", "employer@gig.com", "wrong_pass", Role.EMPLOYER)

		`when`(USER_REPO.findByEmail(request.email))
			.thenReturn(MOCK_EMPLOYER_USER)
		`when`(ENCODER.matches(request.password, MOCK_EMPLOYER_USER.passwordHash))
			.thenReturn(false)

		assertThrows<SecurityException> { AUTH_SERVICE.loginUser(request) }
	}
}