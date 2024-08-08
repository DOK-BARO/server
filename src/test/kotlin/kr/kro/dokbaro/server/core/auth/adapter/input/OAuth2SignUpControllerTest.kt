package kr.kro.dokbaro.server.core.auth.adapter.input

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import kr.kro.dokbaro.server.configuration.docs.Path
import kr.kro.dokbaro.server.configuration.docs.RestDocsTest
import kr.kro.dokbaro.server.core.auth.adapter.input.web.OAuth2SignUpController
import kr.kro.dokbaro.server.core.auth.adapter.input.web.dto.ProviderAuthorizationTokenRequest
import kr.kro.dokbaro.server.core.auth.application.port.input.OAuth2SignUpUseCase
import kr.kro.dokbaro.server.core.token.domain.AuthToken
import kr.kro.dokbaro.server.global.AuthProvider
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.requestFields
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(OAuth2SignUpController::class)
class OAuth2SignUpControllerTest : RestDocsTest() {
	@MockkBean
	lateinit var oAuth2SignUpUseCase: OAuth2SignUpUseCase

	init {
		"회원가입을 수행한다" {
			every { oAuth2SignUpUseCase.signUp(any()) } returns AuthToken("access-token", "refresh-token")

			val body = ProviderAuthorizationTokenRequest("mockToken", "http://localhost:5173/oauth2/redirected/kakao")

			performPost(Path("/auth/oauth2/signup/{provider}", AuthProvider.KAKAO.name), body)
				.andExpect(status().isOk)
				.andDo(
					print(
						"auth/oauth2-signup",
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