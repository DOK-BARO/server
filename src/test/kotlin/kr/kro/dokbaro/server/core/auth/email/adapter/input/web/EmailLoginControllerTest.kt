package kr.kro.dokbaro.server.core.auth.email.adapter.input.web

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import kr.kro.dokbaro.server.common.http.jwt.JwtResponseGenerator
import kr.kro.dokbaro.server.configuration.docs.Path
import kr.kro.dokbaro.server.configuration.docs.RestDocsTest
import kr.kro.dokbaro.server.core.auth.email.application.port.input.EmailLoginUseCase
import kr.kro.dokbaro.server.core.auth.email.application.port.input.dto.EmailLoginCommand
import kr.kro.dokbaro.server.fixture.domain.authTokenFixture
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.requestFields
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(EmailLoginController::class)
class EmailLoginControllerTest : RestDocsTest() {
	@MockkBean
	lateinit var emailLoginUseCase: EmailLoginUseCase

	@MockkBean
	lateinit var jwtResponseGenerator: JwtResponseGenerator

	init {
		"login을 수행한다" {
			every { emailLoginUseCase.login(any()) } returns authTokenFixture()
			every { jwtResponseGenerator.getResponseBuilder(any(), any()) } returns
				ResponseEntity
					.ok()
					.header(HttpHeaders.SET_COOKIE, "Authorization=access-token;")
					.header(HttpHeaders.SET_COOKIE, "Refresh=refresh-token;")

			val body = EmailLoginCommand("aaa@gmail.com", "password")

			performPost(Path("/auth/email/login"), body)
				.andExpect(status().isOk)
				.andDo(
					print(
						"auth/email-login",
						requestFields(
							fieldWithPath("email")
								.type(JsonFieldType.STRING)
								.description("이메일"),
							fieldWithPath("password")
								.type(JsonFieldType.STRING)
								.description("비밀번호"),
						),
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