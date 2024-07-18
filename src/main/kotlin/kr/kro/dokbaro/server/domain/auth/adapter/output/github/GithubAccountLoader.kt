package kr.kro.dokbaro.server.domain.auth.adapter.output.github

import kr.kro.dokbaro.server.domain.auth.adapter.output.github.external.GithubResourceClient
import kr.kro.dokbaro.server.domain.auth.adapter.output.github.external.provideraccount.GithubAccount
import kr.kro.dokbaro.server.domain.auth.model.ProviderAccount
import kr.kro.dokbaro.server.domain.auth.port.output.LoadProviderAccountPort
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