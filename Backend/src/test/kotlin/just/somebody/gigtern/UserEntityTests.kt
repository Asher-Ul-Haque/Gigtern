package just.somebody.gigtern

import just.somebody.gigtern.domain.entities.UserEntity
import just.somebody.gigtern.domain.repositories.UserRepository
import just.somebody.gigtern.utils.Logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@DataJpaTest
class UserRepositoryTest @Autowired constructor(private val REPO: UserRepository)
{
	@Test
	fun `when saving User entity then it can be retrieved by ID`()
	{
		Logger.LOG_DEBUG("Creating a User object with sample data")
		val initialUser = UserEntity(
			rollNo            = 2023151,
			name              = "Asher Ul Haque",
			phoneNumber       = 9911800581,
			email             = "asher23151@iiitd.ac.in",
			gender            = UserGender.MALE,
			role              = UserRole.BOTH,
			reputationScore   = 4.5f,
			cancellationRate  = 0.05f,
			onTimeRate        = 0.95f
		)

		Logger.LOG_DEBUG("Save the entity")
		val savedUser = REPO.save(initialUser)

		Logger.LOG_DEBUG("Check whether the entity saved")
		assertNotNull(savedUser)
		assertEquals(initialUser.rollNo, savedUser.rollNo)
		assertEquals(UserRole.BOTH, savedUser.role)

		Logger.LOG_DEBUG("Retrieve the user and verify data integrity")
		val foundUser = REPO.findById(initialUser.rollNo).orElse(null)
		assertNotNull(foundUser)
		assertEquals(savedUser.name, foundUser.name)
		assertEquals(UserGender.MALE, foundUser.gender)
	}

	@Test
	fun `when finding by email then correct user is returned`()
	{
		Logger.LOG_DEBUG("Create a test user")
		val emailToFind = "test.user@iiitd.ac.in"
		val user = UserEntity(
			rollNo        = 2023151,
			name          = "Test User",
			phoneNumber   = 99999999,
			email         = emailToFind,
			gender        = UserGender.MALE,
			role          = UserRole.PASSENGER)
		REPO.save(user)

		Logger.LOG_DEBUG("Find by email")
		val foundUser = REPO.findByEmail(emailToFind)

		Logger.LOG_DEBUG("Assert")
		assertNotNull(foundUser)
		assertEquals(2023151, foundUser.rollNo)
	}
}