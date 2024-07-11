package kr.kro.dokbaro.server.domain.auth.model.service.oauth2

import kr.kro.dokbaro.server.domain.auth.port.input.FindOAuth2RedirectUriUseCase
import org.springframework.stereotype.Service

@Service
class OAuth2RedirectService(
	private val strategies: Map<String, OAuth2RedirectStrategy>,
) : FindOAuth2RedirectUriUseCase {
	override fun getRedirectUri(provider: String): String =
		strategies["${provider}RedirectStrategy"]?.getUri() ?: throw RuntimeException()
}