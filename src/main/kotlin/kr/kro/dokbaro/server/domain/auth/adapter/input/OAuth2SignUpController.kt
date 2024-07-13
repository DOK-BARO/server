package kr.kro.dokbaro.server.domain.auth.adapter.input

import kr.kro.dokbaro.server.domain.auth.model.service.oauth2.OAuth2SignUpService
import kr.kro.dokbaro.server.domain.auth.port.input.dto.ProviderAuthorizationCommand
import kr.kro.dokbaro.server.domain.token.model.AuthToken
import kr.kro.dokbaro.server.global.AuthProvider
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth/oauth2/signup")
class OAuth2SignUpController(
	private val service: OAuth2SignUpService,
) {
	@PostMapping("{provider}")
	fun oauth2SignUp(
		@PathVariable provider: AuthProvider,
		code: String,
	): AuthToken = service.signUp(ProviderAuthorizationCommand(provider, code))
}