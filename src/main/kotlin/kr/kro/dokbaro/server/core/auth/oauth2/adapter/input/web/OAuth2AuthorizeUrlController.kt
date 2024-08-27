package kr.kro.dokbaro.server.core.auth.oauth2.adapter.input.web

import kr.kro.dokbaro.server.common.type.AuthProvider
import kr.kro.dokbaro.server.core.auth.oauth2.adapter.input.web.dto.AuthorizeUrlResponse
import kr.kro.dokbaro.server.core.auth.oauth2.application.port.input.FindOAuth2AuthorizeUrlUseCase
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth/oauth2/authorize")
class OAuth2AuthorizeUrlController(
	private val findOauth2AuthorizeUrlUseCase: FindOAuth2AuthorizeUrlUseCase,
) {
	@GetMapping("/{provider}")
	fun getAuthorizeUrl(
		@PathVariable provider: AuthProvider,
		@RequestParam redirectUrl: String,
	) = AuthorizeUrlResponse(
		findOauth2AuthorizeUrlUseCase.getUrl(
			provider,
			redirectUrl,
		),
	)
}