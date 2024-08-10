package kr.kro.dokbaro.server.core.auth.adapter.input

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import kr.kro.dokbaro.server.common.type.AuthProvider
import kr.kro.dokbaro.server.configuration.docs.Path
import kr.kro.dokbaro.server.configuration.docs.RestDocsTest
import kr.kro.dokbaro.server.core.auth.adapter.input.web.JwtResponseGenerator
import kr.kro.dokbaro.server.core.auth.adapter.input.web.OAuth2LoginController
import kr.kro.dokbaro.server.core.auth.adapter.input.web.dto.ProviderAuthorizationTokenRequest
import kr.kro.dokbaro.server.core.auth.application.port.input.OAuth2LoginUseCase
import kr.kro.dokbaro.server.core.auth.application.port.input.dto.LoadProviderAccountCommand
import kr.kro.dokbaro.server.core.token.domain.AuthToken
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.requestFields
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(OAuth2LoginController::class)
class OAuth2LoginControllerTest : RestDocsTest() {
	@MockkBean
	lateinit var loginUseCase: OAuth2LoginUseCase

	@MockkBean
	lateinit var jwtResponseGenerator: JwtResponseGenerator

	init {
		"login을 수행한다" {
			every { loginUseCase.login(any(LoadProviderAccountCommand::class)) } returns
				AuthToken(
					"jwt.access.token",
					"uuid-refresh-token",
				)
			every { jwtResponseGenerator.getResponseBuilder(any(), any()) } returns
				ResponseEntity
					.ok()
					.header(HttpHeaders.SET_COOKIE, "Authorization=access-token;")
					.header(HttpHeaders.SET_COOKIE, "Refresh=refresh-token;")

			val body = ProviderAuthorizationTokenRequest("mockToken", "http://localhost:5173/oauth2/redirected/kakao")

			performPost(Path("/auth/oauth2/login/{provider}", AuthProvider.KAKAO.name), body)
				.andExpect(status().isOk)
				.andDo(
					print(
						"auth/oauth2-login",
						requestFields(
							fieldWithPath("token")
								.type(JsonFieldType.STRING)
								.description("provider authorization token"),
							fieldWithPath("redirectUrl")
								.type(JsonFieldType.STRING)
								.description("client redirect URL"),
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