package kr.kro.dokbaro.server.domain.auth.adapter.input

import com.ninjasquad.springmockk.MockkBean
import kr.kro.dokbaro.server.abstract.Path
import kr.kro.dokbaro.server.abstract.RestDocsTest
import kr.kro.dokbaro.server.domain.auth.adapter.input.dto.ProviderAuthorizationTokenRequest
import kr.kro.dokbaro.server.domain.auth.port.input.OAuth2LoginUseCase
import kr.kro.dokbaro.server.global.AuthProvider
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(OAuth2LoginController::class)
class OAuth2LoginControllerTest : RestDocsTest() {
	@MockkBean
	lateinit var loginUseCase: OAuth2LoginUseCase

	init {
		"회원 탈퇴를 수행한다" {
			val body = ProviderAuthorizationTokenRequest("mockToken")
			val action = performPost(Path("/auth/oauth2/login/{provider}", AuthProvider.KAKAO.name), body)

			action.andExpect(status().isOk)

			action.andDo(
				print(
					"auth/oauth2-login",
				),
			)
		}
	}
}