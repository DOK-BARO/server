package kr.kro.dokbaro.server.core.auth.application.service.oauth2.authorize

import kr.kro.dokbaro.server.core.auth.application.port.input.dto.LoadProviderAccountCommand
import kr.kro.dokbaro.server.core.auth.application.port.out.LoadProviderAccountPort
import kr.kro.dokbaro.server.core.auth.application.port.out.LoadProviderResourceTokenPort
import kr.kro.dokbaro.server.core.auth.domain.ProviderAccount
import org.springframework.stereotype.Component

@Component
class OAuth2AccountLoader(
	private val loadProviderResourceTokenPort: LoadProviderResourceTokenPort,
	private val loadProviderAccountPort: LoadProviderAccountPort,
) {
	fun get(command: LoadProviderAccountCommand): ProviderAccount {
		val token: String =
			loadProviderResourceTokenPort.getToken(
				provider = command.provider,
				authorizationToken = command.token,
				redirectUrl = command.redirectUrl,
			)

		return loadProviderAccountPort.getProviderAccount(
			provider = command.provider,
			accessToken = token,
		)
	}
}