package kr.kro.dokbaro.server.domain.auth.adapter.input

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import kr.kro.dokbaro.server.configuration.docs.Path
import kr.kro.dokbaro.server.configuration.docs.RestDocsTest
import kr.kro.dokbaro.server.domain.auth.port.input.FindOAuth2AuthorizeUrlUseCase
import kr.kro.dokbaro.server.global.AuthProvider
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(OAuth2AuthorizeUrlController::class)
class OAuth2AuthorizeControllerTest : RestDocsTest() {
	@MockkBean
	lateinit var findOauth2AuthorizeUrlUseCase: FindOAuth2AuthorizeUrlUseCase

	init {
		"provider에 해당하는 Authorize page url을 발급받는다" {
			every { findOauth2AuthorizeUrlUseCase.get(any(AuthProvider::class)) } returns
				"https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=adfs" +
				"&redirect_uri=http://dev.dokbaro.kro.kr&scope=image,name"

			performGet(Path("/auth/oauth2/authorize/{provider}", AuthProvider.KAKAO.name))
				.andExpect(status().isOk)
				.andDo(
					print(
						"auth/get-provider-authorize-url",
						responseFields(
							fieldWithPath("url")
								.type(JsonFieldType.STRING)
								.description("provider authorize url"),
						),
					),
				)
		}
	}
}