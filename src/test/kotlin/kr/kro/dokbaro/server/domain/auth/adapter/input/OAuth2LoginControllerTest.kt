package kr.kro.dokbaro.server.domain.auth.adapter.input

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import kr.kro.dokbaro.server.configuration.docs.Path
import kr.kro.dokbaro.server.configuration.docs.RestDocsTest
import kr.kro.dokbaro.server.domain.auth.adapter.input.dto.ProviderAuthorizationTokenRequest
import kr.kro.dokbaro.server.domain.auth.port.input.OAuth2LoginUseCase
import kr.kro.dokbaro.server.domain.auth.port.input.dto.ProviderAuthorizationCommand
import kr.kro.dokbaro.server.domain.token.model.AuthToken
import kr.kro.dokbaro.server.global.AuthProvider
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.requestFields
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(OAuth2LoginController::class)
class OAuth2LoginControllerTest : RestDocsTest() {
	@MockkBean
	lateinit var loginUseCase: OAuth2LoginUseCase

	init {
		"login을 수행한다" {
			every { loginUseCase.login(any(ProviderAuthorizationCommand::class)) } returns
				AuthToken(
					"jwt.access.token",
					"uuid-refresh-token",
				)

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
							fieldWithPath("accessToken")
								.type(JsonFieldType.STRING)
								.description("accessToken (JWT)"),
							fieldWithPath("refreshToken")
								.type(JsonFieldType.STRING)
								.description("refresh token (UUID)"),
						),
					),
				)
		}
	}
}