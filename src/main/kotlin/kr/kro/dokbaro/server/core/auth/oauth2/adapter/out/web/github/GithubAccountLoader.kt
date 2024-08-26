package kr.kro.dokbaro.server.core.auth.oauth2.adapter.out.web.github

import kr.kro.dokbaro.server.common.type.AuthProvider
import kr.kro.dokbaro.server.core.auth.oauth2.adapter.out.web.EmailNotExistException
import kr.kro.dokbaro.server.core.auth.oauth2.adapter.out.web.NickNameNotExistException
import kr.kro.dokbaro.server.core.auth.oauth2.adapter.out.web.ProviderAccountLoader
import kr.kro.dokbaro.server.core.auth.oauth2.adapter.out.web.github.external.GithubResourceClient
import kr.kro.dokbaro.server.core.auth.oauth2.adapter.out.web.github.external.resource.GithubAccount
import kr.kro.dokbaro.server.core.auth.oauth2.application.port.dto.OAuth2ProviderAccount
import org.springframework.stereotype.Component

@Component
class GithubAccountLoader(
	private val resourceClient: GithubResourceClient,
) : ProviderAccountLoader {
	override fun get(accessToken: String): OAuth2ProviderAccount {
		val account: GithubAccount = resourceClient.getUserProfiles(accessToken)

		return OAuth2ProviderAccount(
			AuthProvider.GITHUB,
			account.id.toString(),
			account.name ?: throw NickNameNotExistException(),
			account.email ?: throw EmailNotExistException(),
			account.avatarUrl,
		)
	}
}