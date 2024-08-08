package kr.kro.dokbaro.server.core.auth.adapter.out.web.google

import kr.kro.dokbaro.server.core.auth.adapter.out.web.google.external.GoogleResourceClient
import kr.kro.dokbaro.server.core.auth.adapter.out.web.google.external.resource.GoogleAccount
import kr.kro.dokbaro.server.core.auth.application.port.out.LoadProviderAccountPort
import kr.kro.dokbaro.server.core.auth.domain.ProviderAccount
import kr.kro.dokbaro.server.global.AuthProvider
import org.springframework.stereotype.Component

@Component
class GoogleAccountLoader(
	private val resourceClient: GoogleResourceClient,
) : LoadProviderAccountPort {
	override fun getAttributes(accessToken: String): ProviderAccount {
		val account: GoogleAccount = resourceClient.getUserProfiles(accessToken)

		return ProviderAccount(
			AuthProvider.GOOGLE,
			account.id,
			account.name,
			account.email,
			account.picture,
		)
	}
}