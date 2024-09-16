package kr.kro.dokbaro.server.core.auth.oauth2.adapter.input.web

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import kr.kro.dokbaro.server.common.type.AuthProvider
import kr.kro.dokbaro.server.configuration.docs.Path
import kr.kro.dokbaro.server.configuration.docs.RestDocsTest
import kr.kro.dokbaro.server.core.auth.oauth2.adapter.input.web.dto.ProviderAuthorizationTokenRequest
import kr.kro.dokbaro.server.core.auth.oauth2.application.port.input.OAuth2SignUpUseCase
import kr.kro.dokbaro.server.fixture.domain.authTokenFixture
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.requestFields
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(OAuth2SignUpController::class)
class OAuth2SignUpControllerTest : RestDocsTest() {
	@MockkBean
	lateinit var oAuth2SignUpUseCase: OAuth2SignUpUseCase

	@MockkBean
	lateinit var jwtResponseGenerator: JwtResponseGenerator

	init {
		"회원가입을 수행한다" {
			every { oAuth2SignUpUseCase.signUp(any()) } returns authTokenFixture()
			every { jwtResponseGenerator.getResponseBuilder(any(), any()) } returns
				ResponseEntity
					.ok()
					.header(HttpHeaders.SET_COOKIE, "Authorization=access-token;")
					.header(HttpHeaders.SET_COOKIE, "Refresh=refresh-token;")
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
							fieldWithPath("message")
								.type(JsonFieldType.STRING)
								.description("message"),
						),
					),
				)
		}
	}
}