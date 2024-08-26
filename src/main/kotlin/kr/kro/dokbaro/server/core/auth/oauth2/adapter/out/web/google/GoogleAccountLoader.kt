package kr.kro.dokbaro.server.core.auth.oauth2.adapter.out.web.google

import kr.kro.dokbaro.server.common.type.AuthProvider
import kr.kro.dokbaro.server.core.auth.oauth2.adapter.out.web.EmailNotExistException
import kr.kro.dokbaro.server.core.auth.oauth2.adapter.out.web.NickNameNotExistException
import kr.kro.dokbaro.server.core.auth.oauth2.adapter.out.web.ProviderAccountLoader
import kr.kro.dokbaro.server.core.auth.oauth2.adapter.out.web.google.external.GoogleResourceClient
import kr.kro.dokbaro.server.core.auth.oauth2.adapter.out.web.google.external.resource.GoogleAccount
import kr.kro.dokbaro.server.core.auth.oauth2.application.port.dto.OAuth2ProviderAccount
import org.springframework.stereotype.Component

@Component
class GoogleAccountLoader(
	private val resourceClient: GoogleResourceClient,
) : ProviderAccountLoader {
	override fun get(accessToken: String): OAuth2ProviderAccount {
		val account: GoogleAccount = resourceClient.getUserProfiles(accessToken)

		return OAuth2ProviderAccount(
			AuthProvider.GOOGLE,
			account.id,
			account.name ?: throw NickNameNotExistException(),
			account.email ?: throw EmailNotExistException(),
			account.picture,
		)
	}
}