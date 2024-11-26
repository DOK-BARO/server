package kr.kro.dokbaro.server.core.token.adapter.input.web

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import kr.kro.dokbaro.server.common.http.jwt.JwtResponseEntityGenerator
import kr.kro.dokbaro.server.configuration.docs.Path
import kr.kro.dokbaro.server.configuration.docs.RestDocsTest
import kr.kro.dokbaro.server.core.token.application.port.input.ReGenerateAuthTokenUseCase
import kr.kro.dokbaro.server.fixture.domain.authTokenFixture
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(TokenController::class)
class TokenControllerTest : RestDocsTest() {
	@MockkBean lateinit var reGenerateAuthTokenUseCase: ReGenerateAuthTokenUseCase

	@MockkBean lateinit var jwtResponseEntityGenerator: JwtResponseEntityGenerator

	init {
		"token 재발급을 수행한다" {
			every { jwtResponseEntityGenerator.getResponseBuilder(any(), any()) } returns
				ResponseEntity
					.ok()
					.header(HttpHeaders.SET_COOKIE, "Authorization=access-token;")
					.header(HttpHeaders.SET_COOKIE, "Refresh=refresh-token;")

			every { reGenerateAuthTokenUseCase.reGenerate(any()) } returns authTokenFixture()

			val cookie = mapOf("Refresh" to "refresh-token")

			performPost(Path("/token/refresh"), cookie = cookie)
				.andExpect(status().isOk)
				.andDo(
					print(
						"token/refresh",
						responseFields(
							fieldWithPath("message")
								.type(JsonFieldType.STRING)
								.description("message"),
						),
					),
				)
		}
	}
}