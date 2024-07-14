package kr.kro.dokbaro.server.domain.auth.adapter.input

import kr.kro.dokbaro.server.domain.auth.adapter.input.dto.RedirectUrlResponse
import kr.kro.dokbaro.server.domain.auth.port.input.FindOAuth2RedirectUriUseCase
import kr.kro.dokbaro.server.global.AuthProvider
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth/oauth2/redirect")
class OAuth2RedirectedController(
	private val findOauth2RedirectUriUseCase: FindOAuth2RedirectUriUseCase,
) {
	@GetMapping("/{provider}")
	@ResponseStatus(HttpStatus.FOUND)
	fun redirected(
		@PathVariable provider: AuthProvider,
	) = RedirectUrlResponse(findOauth2RedirectUriUseCase.getRedirectUri(provider))
}