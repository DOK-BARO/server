package kr.kro.dokbaro.server.core.emailauthentication.adapter.input.web

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import kr.kro.dokbaro.server.configuration.docs.Path
import kr.kro.dokbaro.server.configuration.docs.RestDocsTest
import kr.kro.dokbaro.server.core.emailauthentication.adapter.input.web.dto.CreateEmailRequest
import kr.kro.dokbaro.server.core.emailauthentication.adapter.input.web.dto.MatchEmailCodeRequest
import kr.kro.dokbaro.server.core.emailauthentication.application.port.input.CreateEmailAuthenticationUseCase
import kr.kro.dokbaro.server.core.emailauthentication.application.port.input.MatchCodeUseCase
import kr.kro.dokbaro.server.core.emailauthentication.application.port.input.RecreateEmailAuthenticationUseCase
import kr.kro.dokbaro.server.core.emailauthentication.application.port.input.dto.MatchResponse
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.requestFields
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(EmailAuthenticationController::class)
class EmailAuthenticationControllerTest : RestDocsTest() {
	@MockkBean
	lateinit var createEmailAuthenticationUseCase: CreateEmailAuthenticationUseCase

	@MockkBean
	lateinit var matchCodeUseCase: MatchCodeUseCase

	@MockkBean
	lateinit var recreateEmailAuthenticationUseCase: RecreateEmailAuthenticationUseCase

	init {
		"메일 인증을 생성한다" {
			every { createEmailAuthenticationUseCase.create(any()) } returns Unit
			val request =
				CreateEmailRequest(
					email = "test@example.com",
				)

			performPost(Path("/email-authentications"), request)
				.andExpect(status().isCreated)
				.andDo(
					print(
						"email-authentication/create-email-authentication",
						requestFields(
							fieldWithPath("email")
								.type(JsonFieldType.STRING)
								.description("이메일"),
						),
					),
				)
		}

		"인증코드 일치 여부를 확인한다" {
			every { matchCodeUseCase.match(any(), any()) } returns MatchResponse(true)
			val request =
				MatchEmailCodeRequest(
					email = "test@example.com",
					code = "ABCDEF",
				)

			performPost(Path("/email-authentications/match-code"), request)
				.andExpect(status().isNoContent)
				.andDo(
					print(
						"email-authentication/match-email-code",
						requestFields(
							fieldWithPath("email")
								.type(JsonFieldType.STRING)
								.description("이메일"),
							fieldWithPath("code")
								.type(JsonFieldType.STRING)
								.description("인증 코드"),
						),
						responseFields(
							fieldWithPath("result")
								.type(JsonFieldType.BOOLEAN)
								.description("인증코드 일치 여부"),
						),
					),
				)
		}

		"인증코드를 재생성한다" {
			every { recreateEmailAuthenticationUseCase.recreate(any()) } returns Unit

			val request =
				CreateEmailRequest(
					email = "test@example.com",
				)

			performPost(Path("/email-authentications/recreate"), request)
				.andExpect(status().isNoContent)
				.andDo(
					print(
						"email-authentication/recreate-email-authentication",
						requestFields(
							fieldWithPath("email")
								.type(JsonFieldType.STRING)
								.description("이메일"),
						),
					),
				)
		}
	}
}