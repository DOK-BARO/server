package kr.kro.dokbaro.server.domain.auth.model.service.oauth2

import kr.kro.dokbaro.server.domain.auth.model.ProviderAccount
import kr.kro.dokbaro.server.domain.auth.port.input.dto.ProviderAuthorizationCommand
import kr.kro.dokbaro.server.domain.auth.port.output.LoadProviderAccountPort
import kr.kro.dokbaro.server.domain.auth.port.output.LoadProviderResourceTokenPort
import org.springframework.stereotype.Component

@Component
class ProviderAccountLoader(
	private val resourceTokenPort: Map<String, LoadProviderResourceTokenPort>,
	private val accountPort: Map<String, LoadProviderAccountPort>,
) {
	fun load(command: ProviderAuthorizationCommand): ProviderAccount {
		val resourceToken: String =
			resourceTokenPort["${command.provider.name.lowercase()}ResourceTokenLoader"]!!
				.getToken(command.token)

		return accountPort["${command.provider.name.lowercase()}AccountLoader"]!!
			.getAttributes(resourceToken)
	}
}