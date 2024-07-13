package kr.kro.dokbaro.server.domain.auth.model.service.oauth2.redirected

import kr.kro.dokbaro.server.domain.auth.port.input.FindOAuth2RedirectUriUseCase
import kr.kro.dokbaro.server.global.AuthProvider
import org.springframework.stereotype.Service

@Service
class OAuth2RedirectService(
	private val strategies: Map<String, OAuth2RedirectStrategy>,
) : FindOAuth2RedirectUriUseCase {
	override fun getRedirectUri(provider: AuthProvider): String =
		strategies["${provider}RedirectStrategy"]?.getUri() ?: throw RuntimeException()
}