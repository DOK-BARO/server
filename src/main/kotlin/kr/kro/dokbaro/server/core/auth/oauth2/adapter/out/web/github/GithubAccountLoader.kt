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
	override fun getAccount(accessToken: String): OAuth2ProviderAccount {
		val account: GithubAccount = resourceClient.getUserProfiles(accessToken)

		return OAuth2ProviderAccount(
			provider = AuthProvider.GITHUB,
			id = account.id.toString(),
			name = account.name ?: throw NickNameNotExistException(),
			email = account.email ?: throw EmailNotExistException(),
			profileImage = account.avatarUrl,
		)
	}
}