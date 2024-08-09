package kr.kro.dokbaro.server.core.auth.adapter.out.web.github

import kr.kro.dokbaro.server.common.type.AuthProvider
import kr.kro.dokbaro.server.core.auth.adapter.out.web.ProviderAccountLoader
import kr.kro.dokbaro.server.core.auth.adapter.out.web.github.external.GithubResourceClient
import kr.kro.dokbaro.server.core.auth.adapter.out.web.github.external.resource.GithubAccount
import kr.kro.dokbaro.server.core.auth.domain.ProviderAccount
import org.springframework.stereotype.Component

@Component
class GithubAccountLoader(
	private val resourceClient: GithubResourceClient,
) : ProviderAccountLoader {
	override fun get(accessToken: String): ProviderAccount {
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