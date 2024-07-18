package kr.kro.dokbaro.server.domain.auth.adapter.output.google

import kr.kro.dokbaro.server.domain.auth.adapter.output.google.external.GoogleResourceClient
import kr.kro.dokbaro.server.domain.auth.adapter.output.google.external.provideraccount.GoogleAccount
import kr.kro.dokbaro.server.domain.auth.model.ProviderAccount
import kr.kro.dokbaro.server.domain.auth.port.output.LoadProviderAccountPort
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
			account.sub,
			account.name,
			account.email,
			account.picture,
		)
	}
}