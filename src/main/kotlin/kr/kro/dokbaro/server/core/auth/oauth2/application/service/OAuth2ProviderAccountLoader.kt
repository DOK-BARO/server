package kr.kro.dokbaro.server.core.auth.oauth2.application.service

import kr.kro.dokbaro.server.core.auth.oauth2.application.port.dto.LoadProviderAccountCommand
import kr.kro.dokbaro.server.core.auth.oauth2.application.port.dto.OAuth2ProviderAccount
import kr.kro.dokbaro.server.core.auth.oauth2.application.port.out.provider.LoadOAuth2ProviderAccountPort
import kr.kro.dokbaro.server.core.auth.oauth2.application.port.out.provider.LoadOAuth2ProviderResourceTokenPort
import org.springframework.stereotype.Component

@Component
class OAuth2ProviderAccountLoader(
	private val loadOAuth2ProviderResourceTokenPort: LoadOAuth2ProviderResourceTokenPort,
	private val loadOAuth2ProviderAccountPort: LoadOAuth2ProviderAccountPort,
) {
	fun getAccount(command: LoadProviderAccountCommand): OAuth2ProviderAccount {
		val token: String =
			loadOAuth2ProviderResourceTokenPort.getToken(
				provider = command.provider,
				authorizationToken = command.token,
				redirectUrl = command.redirectUrl,
			)

		return loadOAuth2ProviderAccountPort.getProviderAccount(
			provider = command.provider,
			accessToken = token,
		)
	}
}