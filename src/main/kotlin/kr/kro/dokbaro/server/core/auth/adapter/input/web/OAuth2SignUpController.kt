package kr.kro.dokbaro.server.core.auth.adapter.input.web

import kr.kro.dokbaro.server.core.auth.adapter.input.web.dto.ProviderAuthorizationTokenRequest
import kr.kro.dokbaro.server.core.auth.application.port.input.OAuth2SignUpUseCase
import kr.kro.dokbaro.server.core.auth.application.port.input.dto.ProviderAuthorizationCommand
import kr.kro.dokbaro.server.core.token.domain.AuthToken
import kr.kro.dokbaro.server.global.AuthProvider
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth/oauth2/signup")
class OAuth2SignUpController(
	private val oAuth2SignUpUseCase: OAuth2SignUpUseCase,
) {
	@PostMapping("/{provider}")
	fun oauth2SignUp(
		@PathVariable provider: AuthProvider,
		@RequestBody body: ProviderAuthorizationTokenRequest,
	): AuthToken = oAuth2SignUpUseCase.signUp(ProviderAuthorizationCommand(provider, body.token, body.redirectUrl))
}