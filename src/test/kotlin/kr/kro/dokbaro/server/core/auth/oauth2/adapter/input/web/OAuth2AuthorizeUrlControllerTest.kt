package kr.kro.dokbaro.server.core.auth.oauth2.adapter.input.web

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import kr.kro.dokbaro.server.common.type.AuthProvider
import kr.kro.dokbaro.server.configuration.docs.Path
import kr.kro.dokbaro.server.configuration.docs.RestDocsTest
import kr.kro.dokbaro.server.core.auth.oauth2.application.port.input.FindOAuth2AuthorizeUrlUseCase
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.restdocs.request.RequestDocumentation.parameterWithName
import org.springframework.restdocs.request.RequestDocumentation.queryParameters
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(OAuth2AuthorizeUrlController::class)
class OAuth2AuthorizeUrlControllerTest : RestDocsTest() {
	@MockkBean
	lateinit var findOauth2AuthorizeUrlUseCase: FindOAuth2AuthorizeUrlUseCase

	init {
		"provider에 해당하는 Authorize page url을 발급받는다" {
			every { findOauth2AuthorizeUrlUseCase.getUrl(any(AuthProvider::class), any()) } returns
				"https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=adfs" +
				"&redirect_uri=http://dev.dokbaro.kro.kr&scope=image,name"
			val param = mapOf("redirectUrl" to "http://dev.dokbaro.kro.kr/redirected/kakao")
			performGet(Path("/auth/oauth2/authorize/{provider}", AuthProvider.KAKAO.name), param)
				.andExpect(status().isOk)
				.andDo(
					print(
						"auth/get-provider-authorize-url",
						queryParameters(
							parameterWithName("redirectUrl").description("redirect url"),
						),
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