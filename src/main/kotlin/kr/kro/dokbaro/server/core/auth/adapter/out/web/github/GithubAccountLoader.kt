package kr.kro.dokbaro.server.core.auth.adapter.out.web.github

import kr.kro.dokbaro.server.core.auth.adapter.out.web.github.external.GithubResourceClient
import kr.kro.dokbaro.server.core.auth.adapter.out.web.github.external.resource.GithubAccount
import kr.kro.dokbaro.server.core.auth.application.port.out.LoadProviderAccountPort
import kr.kro.dokbaro.server.core.auth.domain.ProviderAccount
import kr.kro.dokbaro.server.global.AuthProvider
import org.springframework.stereotype.Component

@Component
class GithubAccountLoader(
	private val resourceClient: GithubResourceClient,
) : LoadProviderAccountPort {
	override fun getAttributes(accessToken: String): ProviderAccount {
		val account: GithubAccount = resourceClient.getUserProfiles(accessToken)

		return ProviderAccount(
			AuthProvider.GITHUB,
			account.id.toString(),
			account.name,
			account.email,
			account.avatarUrl,
		)
	}
}