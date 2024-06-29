package kr.kro.dokbaro.server

import kr.kro.dokbaro.server.configuration.TestcontainersConfiguration
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import

@Import(TestcontainersConfiguration::class)
@SpringBootTest
class ServerApplicationTests {
	@Test
	fun contextLoads() {
	}
}