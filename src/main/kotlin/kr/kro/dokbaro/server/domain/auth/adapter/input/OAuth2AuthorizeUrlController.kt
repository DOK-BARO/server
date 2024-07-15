package kr.kro.dokbaro.server.domain.auth.adapter.input

import kr.kro.dokbaro.server.domain.auth.adapter.input.dto.AuthorizeUrlResponse
import kr.kro.dokbaro.server.domain.auth.port.input.FindOAuth2AuthorizeUrlUseCase
import kr.kro.dokbaro.server.global.AuthProvider
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth/oauth2/authorize")
class OAuth2AuthorizeUrlController(
	private val findOauth2AuthorizeUrlUseCase: FindOAuth2AuthorizeUrlUseCase,
) {
	@GetMapping("/{provider}")
	fun getAuthorizeUrl(
		@PathVariable provider: AuthProvider,
	) = AuthorizeUrlResponse(findOauth2AuthorizeUrlUseCase.get(provider))
}