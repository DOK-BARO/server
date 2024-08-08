package kr.kro.dokbaro.server.core.auth.application.service.oauth2.authorize

import kr.kro.dokbaro.server.core.auth.application.port.input.FindOAuth2AuthorizeUrlUseCase
import kr.kro.dokbaro.server.global.AuthProvider
import org.springframework.stereotype.Service

@Service
class OAuth2AuthorizeService(
	private val strategies: Map<String, OAuth2AuthorizeStrategy>,
) : FindOAuth2AuthorizeUrlUseCase {
	override fun get(provider: AuthProvider): String =
		strategies["${provider.name.lowercase()}AuthorizeStrategy"]!!.getUri()
}