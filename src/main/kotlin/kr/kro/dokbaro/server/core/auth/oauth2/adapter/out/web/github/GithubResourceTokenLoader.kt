package kr.kro.dokbaro.server.core.auth.oauth2.adapter.out.web.github

import kr.kro.dokbaro.server.core.auth.oauth2.adapter.out.web.ProviderResourceTokenLoader
import kr.kro.dokbaro.server.core.auth.oauth2.adapter.out.web.github.external.GithubAuthorizationClient
import kr.kro.dokbaro.server.core.auth.oauth2.adapter.out.web.github.external.GithubAuthorizationTokenResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class GithubResourceTokenLoader(
	private val authorizationClient: GithubAuthorizationClient,
	@Value("\${oauth2.github.client.id}") private val clientId: String,
	@Value("\${oauth2.github.client.secret}") private val clientSecret: String,
) : ProviderResourceTokenLoader {
	override fun getResource(
		authorizationToken: String,
		redirectUrl: String,
	): String {
		val token: GithubAuthorizationTokenResponse =
			authorizationClient
				.getAuthorizationToken(
					code = authorizationToken,
					clientId = clientId,
					redirectUri = redirectUrl,
					clientSecret = clientSecret,
				)

		return "${token.tokenType} ${token.accessToken}"
	}
}