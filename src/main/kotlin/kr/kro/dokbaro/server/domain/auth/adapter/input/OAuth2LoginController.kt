package kr.kro.dokbaro.server.domain.auth.adapter.input

import kr.kro.dokbaro.server.domain.auth.adapter.input.dto.ProviderAuthorizationTokenRequest
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth/oauth2/login")
class OAuth2LoginController(
	// private val loginUseCase: OAuth2LoginUseCase,
) {
	@PostMapping("/{provider}")
	fun oauth2Login(
		@PathVariable provider: String,
		@RequestBody body: ProviderAuthorizationTokenRequest,
	) {
		// loginUseCase.login(ProviderAuthorizationCommand(provider, body.token))
	}
}