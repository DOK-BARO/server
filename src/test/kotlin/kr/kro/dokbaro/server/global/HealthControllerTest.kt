package kr.kro.dokbaro.server.global

import kr.kro.dokbaro.server.configuration.docs.Path
import kr.kro.dokbaro.server.configuration.docs.RestDocsTest
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(HealthController::class)
class HealthControllerTest : RestDocsTest() {
	init {
		"get health check를 수행한다" {
			performGet(Path("/health-check"))
				.andExpect(status().isOk)
				.andDo(
					print(
						"health-check/get",
					),
				)
		}

		"post health check를 수행한다" {
			performPost(Path("/health-check"))
				.andExpect(status().isOk)
				.andDo(
					print(
						"health-check/post",
					),
				)
		}
	}
}