package just.somebody.gigtern


import just.somebody.gigtern.controllers.dtos.ApplicationRequestDTO
import just.somebody.gigtern.domain.entities.*
import just.somebody.gigtern.domain.enums.ApplicationStatus
import just.somebody.gigtern.domain.enums.GigStatus
import just.somebody.gigtern.domain.enums.PaymentType
import just.somebody.gigtern.domain.enums.Role
import just.somebody.gigtern.domain.repositories.ApplicationRepository
import just.somebody.gigtern.domain.repositories.GigRepository
import just.somebody.gigtern.domain.repositories.StudentRepository
import just.somebody.gigtern.service.ApplicationService
import just.somebody.gigtern.utils.exceptions.ApplicationException
import just.somebody.gigtern.utils.exceptions.AuthorizationException
import just.somebody.gigtern.utils.exceptions.ResourceNotFoundException
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import java.util.*

@ExtendWith(MockitoExtension::class)
class ApplicationServiceTest {

	@Mock private lateinit var applicationRepository: ApplicationRepository
	@Mock private lateinit var gigRepository: GigRepository
	@Mock private lateinit var studentRepository: StudentRepository

	private lateinit var applicationService: ApplicationService

	// Mock Data
	private val GIG_ID = 100L
	private val APPLICATION_ID = 500L
	private val EMPLOYER_USER = UserEntity(
		id = 1L, name = "Boss Man", email = "employer@gig.com", passwordHash = "h", role = Role.EMPLOYER
	)
	private val STUDENT_USER = UserEntity(
		id = 2L, name = "Jane Doe", email = "student@gig.com", passwordHash = "h", role = Role.STUDENT
	)
	private val MOCK_EMPLOYER_PROFILE = EmployerEntity(
		id = 10L, user = EMPLOYER_USER, pointOfContact = "Boss Man", aadharKycUrl = null
	)
	private val MOCK_STUDENT_PROFILE = StudentEntity(
		id = 20L, user = STUDENT_USER
	)
	private val MOCK_OPEN_GIG = GigEntity(
		id = GIG_ID, employer = MOCK_EMPLOYER_PROFILE, title = "Open Gig", description = "Desc",
		paymentType = PaymentType.FIXED, payRate = 50.0f, status = GigStatus.OPEN
	)
	private val MOCK_APPLICATION_REQUEST = ApplicationRequestDTO(
		coverLetter = "I am perfect for this."
	)
	private val MOCK_APPLICATION_ENTITY = ApplicationEntity(
		id = APPLICATION_ID, gig = MOCK_OPEN_GIG, student = MOCK_STUDENT_PROFILE,
		coverLetter = MOCK_APPLICATION_REQUEST.coverLetter
	)

	@BeforeEach
	fun setUp() {
		applicationService = ApplicationService(applicationRepository, gigRepository, studentRepository)
	}

	// --- Apply For Gig Tests ---

	@Test
	fun `applyForGig should successfully create an application`() {
		`when`(studentRepository.findByUser(STUDENT_USER)).thenReturn(MOCK_STUDENT_PROFILE)
		`when`(gigRepository.findById(GIG_ID)).thenReturn(Optional.of(MOCK_OPEN_GIG))
		`when`(applicationRepository.save(any(ApplicationEntity::class.java))).thenReturn(MOCK_APPLICATION_ENTITY)

		val response = applicationService.applyForGig(GIG_ID, MOCK_APPLICATION_REQUEST, STUDENT_USER)

		assertEquals(APPLICATION_ID, response.id)
		assertEquals(ApplicationStatus.APPLIED, response.status)
		verify(applicationRepository, times(1)).save(any(ApplicationEntity::class.java))
	}

	@Test
	fun `applyForGig should throw AuthorizationException if user is an Employer`() {
		assertThrows<AuthorizationException> {
			applicationService.applyForGig(GIG_ID, MOCK_APPLICATION_REQUEST, EMPLOYER_USER)
		}
	}

	@Test
	fun `applyForGig should throw ResourceNotFoundException if Student profile is missing`() {
		`when`(studentRepository.findByUser(STUDENT_USER)).thenReturn(null)
		assertThrows<ResourceNotFoundException> {
			applicationService.applyForGig(GIG_ID, MOCK_APPLICATION_REQUEST, STUDENT_USER)
		}
	}

	@Test
	fun `applyForGig should throw ApplicationException if gig is already matched`() {
		val closedGig = MOCK_OPEN_GIG.copy(status = GigStatus.MATCHED)
		`when`(studentRepository.findByUser(STUDENT_USER)).thenReturn(MOCK_STUDENT_PROFILE)
		`when`(gigRepository.findById(GIG_ID)).thenReturn(Optional.of(closedGig))

		assertThrows<ApplicationException> {
			applicationService.applyForGig(GIG_ID, MOCK_APPLICATION_REQUEST, STUDENT_USER)
		}
	}

	// --- Get Applications For Gig Tests ---

	@Test
	fun `getApplicationsForGig should return applications if user is the gig owner`() {
		// Gig owner is EMPLOYER_USER (ID 1L)
		val appList = listOf(MOCK_APPLICATION_ENTITY)
		`when`(gigRepository.findById(GIG_ID)).thenReturn(Optional.of(MOCK_OPEN_GIG))
		`when`(applicationRepository.findAllByGigId(GIG_ID)).thenReturn(appList)

		val response = applicationService.getApplicationsForGig(GIG_ID, EMPLOYER_USER)

		assertEquals(1, response.size)
		assertEquals(APPLICATION_ID, response[0].id)
	}

	@Test
	fun `getApplicationsForGig should throw AuthorizationException if user is not the gig owner`() {
		// Student user (ID 2L) tries to view the employer's gig applications
		`when`(gigRepository.findById(GIG_ID)).thenReturn(Optional.of(MOCK_OPEN_GIG))

		assertThrows<AuthorizationException> {
			applicationService.getApplicationsForGig(GIG_ID, STUDENT_USER)
		}
	}

	// --- Accept Application Tests ---

	@Test
	fun `acceptApplication should successfully update application and gig status`() {
		// Mock data to return the original application/gig
		`when`(applicationRepository.findById(APPLICATION_ID)).thenReturn(Optional.of(MOCK_APPLICATION_ENTITY))
		// Mock the save calls, returning the *updated* entity for the application
		`when`(applicationRepository.save(any(ApplicationEntity::class.java))).thenAnswer { it.arguments[0] }
		`when`(gigRepository.save(any(GigEntity::class.java))).thenAnswer { it.arguments[0] }

		val response = applicationService.acceptApplication(APPLICATION_ID, EMPLOYER_USER)

		// Assertions on the response DTO
		assertEquals(ApplicationStatus.ACCEPTED, response.status)

		// Verify the database interactions:
		// 1. Application status was updated to ACCEPTED
		verify(applicationRepository).save(argThat { app: ApplicationEntity -> app.status == ApplicationStatus.ACCEPTED })
		// 2. Gig status was updated to MATCHED
		verify(gigRepository).save(argThat { gig: GigEntity -> gig.status == GigStatus.MATCHED })
	}

	@Test
	fun `acceptApplication should throw ApplicationException if gig is already matched`() {
		val matchedGig = MOCK_OPEN_GIG.copy(status = GigStatus.MATCHED)
		val appForMatchedGig = MOCK_APPLICATION_ENTITY.copy(gig = matchedGig)

		`when`(applicationRepository.findById(APPLICATION_ID)).thenReturn(Optional.of(appForMatchedGig))

		assertThrows<ApplicationException> {
			applicationService.acceptApplication(APPLICATION_ID, EMPLOYER_USER)
		}
	}

	@Test
	fun `acceptApplication should throw AuthorizationException if user is not the gig owner`() {
		`when`(applicationRepository.findById(APPLICATION_ID)).thenReturn(Optional.of(MOCK_APPLICATION_ENTITY))

		assertThrows<AuthorizationException> {
			applicationService.acceptApplication(APPLICATION_ID, STUDENT_USER)
		}
	}

	@Test
	fun `acceptApplication should throw ResourceNotFoundException if application not found`() {
		`when`(applicationRepository.findById(APPLICATION_ID)).thenReturn(Optional.empty())

		assertThrows<ResourceNotFoundException> {
			applicationService.acceptApplication(APPLICATION_ID, EMPLOYER_USER)
		}
	}
}