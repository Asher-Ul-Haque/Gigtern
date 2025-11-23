package just.somebody.gigtern

import just.somebody.gigtern.utils.exceptions.AuthorizationException

import just.somebody.gigtern.controllers.dtos.requests.GigRequestDTO
import just.somebody.gigtern.domain.entities.EmployerEntity
import just.somebody.gigtern.domain.entities.GigEntity
import just.somebody.gigtern.domain.entities.UserEntity
import just.somebody.gigtern.domain.enums.PaymentType
import just.somebody.gigtern.domain.enums.Role
import just.somebody.gigtern.domain.repositories.EmployerRepository
import just.somebody.gigtern.domain.repositories.GigRepository
import just.somebody.gigtern.service.GigService
import just.somebody.gigtern.utils.exceptions.ResourceNotFoundException
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class GigServiceTest
{
	@Mock private lateinit var GIG_REPO       : GigRepository
	@Mock private lateinit var EMPLOYER_REPO  : EmployerRepository

	private lateinit var gigService: GigService

	// - - - Mock Data
	private val MOCK_EMPLOYER_USER = UserEntity(
		id = 1L, name = "Boss Man", email = "employer@gig.com",
		passwordHash = "hashed_pass", role = Role.EMPLOYER)
	private val MOCK_STUDENT_USER = UserEntity(
		id = 2L, name = "Jane Doe", email = "student@gig.com",
		passwordHash = "hashed_pass", role = Role.STUDENT)
	private val MOCK_EMPLOYER_PROFILE = EmployerEntity(
		id = 10L, user = MOCK_EMPLOYER_USER, pointOfContact = "Boss Man", aadharKycUrl = null)
	private val MOCK_GIG_REQUEST = GigRequestDTO(
		title = "Test Gig", description = "Desc", paymentType = PaymentType.FIXED, payRate = 50.0f)
	private val MOCK_GIG_ENTITY = GigEntity(
		id = 100L, employer = MOCK_EMPLOYER_PROFILE, title = "Test Gig", description = "Desc",
		paymentType = PaymentType.FIXED, payRate = 50.0f)
	private val MOCK_GIG_ENTITY_2 = GigEntity(
		id = 101L, employer = MOCK_EMPLOYER_PROFILE, title = "Another Gig", description = "Desc",
		paymentType = PaymentType.HOURLY, payRate = 15.0f)

	@BeforeEach
	fun setUp()
	{ gigService = GigService(GIG_REPO, EMPLOYER_REPO) }


	// - - - Create Gig Tests - - -

	@Test
	fun `createGig should successfully create and return a gig DTO`()
	{
		// - - - Mock dependencies
		`when`(EMPLOYER_REPO.findByUser(MOCK_EMPLOYER_USER))
			.thenReturn(MOCK_EMPLOYER_PROFILE)
		`when`(GIG_REPO.save(any(GigEntity::class.java)))
			.thenReturn(MOCK_GIG_ENTITY)

		val response = gigService.createGig(MOCK_GIG_REQUEST, MOCK_EMPLOYER_USER)

		// - - - Assertions
		assertEquals(MOCK_GIG_ENTITY.title, response.title)
		assertEquals(MOCK_EMPLOYER_PROFILE.id, response.employerId)
		verify(GIG_REPO, times(1)).save(any(GigEntity::class.java))
	}

	@Test
	fun `createGig should throw AuthorizationException if user is not an EMPLOYER`()
	{
		// - - - Act & Assert
		assertThrows<AuthorizationException> { gigService.createGig(MOCK_GIG_REQUEST, MOCK_STUDENT_USER) }
		verify(EMPLOYER_REPO, never()).findByUser(any())
		verify(GIG_REPO, never()).save(any())
	}

	@Test
	fun `createGig should throw ResourceNotFoundException if employer profile is missing`()
	{
		// - - - Mock dependencies
		`when`(EMPLOYER_REPO.findByUser(MOCK_EMPLOYER_USER)).thenReturn(null)

		// - - - Act & Assert
		assertThrows<ResourceNotFoundException> { gigService.createGig(MOCK_GIG_REQUEST, MOCK_EMPLOYER_USER) }
		verify(GIG_REPO, never()).save(any())
	}


	// - - - Get All Gigs Tests - - -

	@Test
	fun `getAllGigs should return a list of all gigs`()
	{
		val allGigs = listOf(MOCK_GIG_ENTITY, MOCK_GIG_ENTITY_2)
		`when`(GIG_REPO.findAll()).thenReturn(allGigs)

		val response = gigService.getAllGigs()

		// - - - Assertions
		assertEquals(2, response.size)
		assertEquals("Another Gig", response[1].title)
		verify(GIG_REPO, times(1)).findAll()
	}

	@Test
	fun `getAllGigs should return an empty list if no gigs exist`()
	{
		`when`(GIG_REPO.findAll()).thenReturn(emptyList())

		val response = gigService.getAllGigs()

		// - - - Assertions
		assertTrue(response.isEmpty())
	}


	// - - - Get My Gigs Tests - - -

	@Test
	fun `getMyGigs should return a list of gigs posted by the authenticated employer`()
	{
		val myGigs = listOf(MOCK_GIG_ENTITY, MOCK_GIG_ENTITY_2)

		// - - - Mock dependencies
		`when`(EMPLOYER_REPO.findByUser(MOCK_EMPLOYER_USER))
			.thenReturn(MOCK_EMPLOYER_PROFILE)
		`when`(GIG_REPO.findAllByEmployerId(MOCK_EMPLOYER_PROFILE.id))
			.thenReturn(myGigs)

		val response = gigService.getMyGigs(MOCK_EMPLOYER_USER)

		// - - - Assertions
		assertEquals(2, response.size)
		assertTrue(response.all { it.employerId == MOCK_EMPLOYER_PROFILE.id })
	}

	@Test
	fun `getMyGigs should throw AuthorizationException if user is not an EMPLOYER`()
	{
		// - - - Act & Assert
		assertThrows<AuthorizationException> { gigService.getMyGigs(MOCK_STUDENT_USER) }
		verify(GIG_REPO, never()).findAllByEmployerId(any())
	}
}