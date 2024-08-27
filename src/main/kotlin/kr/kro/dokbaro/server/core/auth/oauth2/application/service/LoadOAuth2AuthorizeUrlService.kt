package kr.kro.dokbaro.server.core.auth.oauth2.application.service

import kr.kro.dokbaro.server.common.type.AuthProvider
import kr.kro.dokbaro.server.core.auth.oauth2.application.port.input.FindOAuth2AuthorizeUrlUseCase
import kr.kro.dokbaro.server.core.auth.oauth2.application.port.out.provider.LoadOAuth2ProviderAuthorizationServerUrlPort
import org.springframework.stereotype.Service

@Service
class LoadOAuth2AuthorizeUrlService(
	private val loadUrlPort: LoadOAuth2ProviderAuthorizationServerUrlPort,
) : FindOAuth2AuthorizeUrlUseCase {
	override fun getUrl(
		provider: AuthProvider,
		redirectUrl: String,
	): String = loadUrlPort.getUrl(provider, redirectUrl)
}