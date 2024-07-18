package kr.kro.dokbaro.server.domain.auth.adapter.output.github

import kr.kro.dokbaro.server.domain.auth.adapter.output.github.external.GithubAuthorizationClient
import kr.kro.dokbaro.server.domain.auth.adapter.output.github.external.GithubAuthorizationTokenResponse
import kr.kro.dokbaro.server.domain.auth.port.output.LoadProviderResourceTokenPort
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class GithubResourceTokenLoader(
	private val authorizationClient: GithubAuthorizationClient,
	@Value("\${oauth2.github.client.id}") private val clientId: String,
	@Value("\${oauth2.github.client.redirect-uri}") private val redirectUri: String,
	@Value("\${oauth2.github.client.secret}") private val clientSecret: String,
) : LoadProviderResourceTokenPort {
	override fun getToken(authorizationToken: String): String {
		val token: GithubAuthorizationTokenResponse =
			authorizationClient
				.getAuthorizationToken(
					authorizationToken,
					clientId,
					redirectUri,
					clientSecret,
				)

		return "${token.tokenType} ${token.accessToken}"
	}
}