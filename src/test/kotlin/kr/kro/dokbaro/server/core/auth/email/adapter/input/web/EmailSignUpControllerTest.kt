package kr.kro.dokbaro.server.core.auth.email.adapter.input.web

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import kr.kro.dokbaro.server.common.http.jwt.JwtResponseGenerator
import kr.kro.dokbaro.server.configuration.docs.Path
import kr.kro.dokbaro.server.configuration.docs.RestDocsTest
import kr.kro.dokbaro.server.core.auth.email.application.port.input.EmailSignUpUseCase
import kr.kro.dokbaro.server.core.auth.email.application.port.input.dto.EmailSignUpCommand
import kr.kro.dokbaro.server.fixture.domain.authTokenFixture
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.requestFields
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(EmailSignUpController::class)
class EmailSignUpControllerTest : RestDocsTest() {
	@MockkBean
	lateinit var emailSignUpUseCase: EmailSignUpUseCase

	@MockkBean
	lateinit var jwtResponseGenerator: JwtResponseGenerator

	init {
		"회원가입을 수행한다" {
			every { emailSignUpUseCase.signUp(any()) } returns authTokenFixture()
			every { jwtResponseGenerator.getResponseBuilder(any(), any()) } returns
				ResponseEntity
					.ok()
					.header(HttpHeaders.SET_COOKIE, "Authorization=access-token;")
					.header(HttpHeaders.SET_COOKIE, "Refresh=refresh-token;")
			val body =
				EmailSignUpCommand(
					"kkk@gmail.com",
					"password",
					"nickname",
					"profile.png",
				)

			performPost(Path("/auth/email/signup"), body)
				.andExpect(status().isOk)
				.andDo(
					print(
						"auth/email-signup",
						requestFields(
							fieldWithPath("email")
								.type(JsonFieldType.STRING)
								.description("email"),
							fieldWithPath("password")
								.type(JsonFieldType.STRING)
								.description("이메일"),
							fieldWithPath("nickname")
								.type(JsonFieldType.STRING)
								.description("별명"),
							fieldWithPath("profileImage")
								.type(JsonFieldType.STRING)
								.description("프로필 사진"),
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