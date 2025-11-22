package just.somebody.rideShareBackend

import just.somebody.rideShareBackend.utils.Logger
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class RideShareBackendApplicationTests
{
	@Test
	fun contextLoads()
	{
		Logger.LOG_DEBUG("The context loads")
	}
}
