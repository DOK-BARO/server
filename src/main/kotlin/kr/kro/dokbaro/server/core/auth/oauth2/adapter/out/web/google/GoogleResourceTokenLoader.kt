package kr.kro.dokbaro.server.core.auth.oauth2.adapter.out.web.google

import kr.kro.dokbaro.server.core.auth.oauth2.adapter.out.web.ProviderResourceTokenLoader
import kr.kro.dokbaro.server.core.auth.oauth2.adapter.out.web.google.external.GoogleAuthorizationClient
import kr.kro.dokbaro.server.core.auth.oauth2.adapter.out.web.google.external.GoogleAuthorizationTokenResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class GoogleResourceTokenLoader(
	private val authorizationClient: GoogleAuthorizationClient,
	@Value("\${oauth2.google.provider.grant-type}") private val grantType: String,
	@Value("\${oauth2.google.client.id}") private val clientId: String,
	@Value("\${oauth2.google.client.secret}") private val clientSecret: String,
) : ProviderResourceTokenLoader {
	override fun getResource(
		authorizationToken: String,
		redirectUrl: String,
	): String {
		val token: GoogleAuthorizationTokenResponse =
			authorizationClient
				.getAuthorizationToken(
					code = authorizationToken,
					grantType = grantType,
					clientId = clientId,
					redirectUri = redirectUrl,
					clientSecret = clientSecret,
				)

		return "${token.tokenType} ${token.accessToken}"
	}
}