package kr.kro.dokbaro.server.core.auth.adapter.out.web.google

import kr.kro.dokbaro.server.common.type.AuthProvider
import kr.kro.dokbaro.server.core.auth.adapter.out.web.ProviderAccountLoader
import kr.kro.dokbaro.server.core.auth.adapter.out.web.google.external.GoogleResourceClient
import kr.kro.dokbaro.server.core.auth.adapter.out.web.google.external.resource.GoogleAccount
import kr.kro.dokbaro.server.core.auth.domain.ProviderAccount
import org.springframework.stereotype.Component

@Component
class GoogleAccountLoader(
	private val resourceClient: GoogleResourceClient,
) : ProviderAccountLoader {
	override fun get(accessToken: String): ProviderAccount {
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