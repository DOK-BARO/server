package kr.kro.dokbaro.server.core.auth.application.service.oauth2.authorize

import kr.kro.dokbaro.server.common.type.AuthProvider
import kr.kro.dokbaro.server.core.auth.application.port.input.FindOAuth2AuthorizeUrlUseCase
import kr.kro.dokbaro.server.core.auth.application.port.out.LoadProviderAuthorizationServerUrlPort
import org.springframework.stereotype.Service

@Service
class LoadOAuth2AuthorizeUrlService(
	private val loadUrlPort: LoadProviderAuthorizationServerUrlPort,
) : FindOAuth2AuthorizeUrlUseCase {
	override fun get(provider: AuthProvider): String = loadUrlPort.getUrl(provider)
}