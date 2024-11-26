package kr.kro.dokbaro.server.core.auth.oauth2.adapter.input.web

import kr.kro.dokbaro.server.common.dto.response.MessageResponse
import kr.kro.dokbaro.server.common.http.jwt.JwtResponseEntityGenerator
import kr.kro.dokbaro.server.common.type.AuthProvider
import kr.kro.dokbaro.server.core.auth.oauth2.adapter.input.web.dto.ProviderAuthorizationTokenRequest
import kr.kro.dokbaro.server.core.auth.oauth2.application.port.dto.LoadProviderAccountCommand
import kr.kro.dokbaro.server.core.auth.oauth2.application.port.input.OAuth2SignUpUseCase
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth/oauth2/signup")
class OAuth2SignUpController(
	private val oAuth2SignUpUseCase: OAuth2SignUpUseCase,
	private val jwtResponseEntityGenerator: JwtResponseEntityGenerator,
) {
	@PostMapping("/{provider}")
	fun oauth2SignUp(
		@PathVariable provider: AuthProvider,
		@RequestBody body: ProviderAuthorizationTokenRequest,
	): ResponseEntity<MessageResponse> {
		val (accessToken: String, refreshToken: String) =
			oAuth2SignUpUseCase.signUp(
				LoadProviderAccountCommand(
					provider,
					body.token,
					body.redirectUrl,
				),
			)

		return jwtResponseEntityGenerator
			.getResponseBuilder(accessToken, refreshToken)
			.body(MessageResponse("SignUp Success / set cookie"))
	}
}