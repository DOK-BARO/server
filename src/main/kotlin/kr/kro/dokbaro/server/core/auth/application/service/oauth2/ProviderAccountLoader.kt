package kr.kro.dokbaro.server.core.auth.application.service.oauth2

import kr.kro.dokbaro.server.core.auth.application.port.input.dto.ProviderAuthorizationCommand
import kr.kro.dokbaro.server.core.auth.application.port.out.LoadProviderAccountPort
import kr.kro.dokbaro.server.core.auth.application.port.out.LoadProviderResourceTokenPort
import kr.kro.dokbaro.server.core.auth.domain.ProviderAccount
import org.springframework.stereotype.Component

@Component
class ProviderAccountLoader(
	private val resourceTokenPort: Map<String, LoadProviderResourceTokenPort>,
	private val accountPort: Map<String, LoadProviderAccountPort>,
) {
	fun load(command: ProviderAuthorizationCommand): ProviderAccount {
		val resourceToken: String =
			resourceTokenPort["${command.provider.name.lowercase()}ResourceTokenLoader"]!!
				.getToken(command.token, command.redirectUrl)

		return accountPort["${command.provider.name.lowercase()}AccountLoader"]!!
			.getAttributes(resourceToken)
	}
}