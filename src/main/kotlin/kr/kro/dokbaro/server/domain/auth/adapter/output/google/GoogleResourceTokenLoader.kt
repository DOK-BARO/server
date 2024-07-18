package kr.kro.dokbaro.server.domain.auth.adapter.output.google

import kr.kro.dokbaro.server.domain.auth.adapter.output.google.external.GoogleAuthorizationClient
import kr.kro.dokbaro.server.domain.auth.adapter.output.google.external.GoogleAuthorizationTokenResponse
import kr.kro.dokbaro.server.domain.auth.port.output.LoadProviderResourceTokenPort
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class GoogleResourceTokenLoader(
	private val authorizationClient: GoogleAuthorizationClient,
	@Value("\${oauth2.google.provider.grant-type}") private val grantType: String,
	@Value("\${oauth2.google.client.id}") private val clientId: String,
	@Value("\${oauth2.google.client.redirect-uri}") private val redirectUri: String,
	@Value("\${oauth2.google.client.secret}") private val clientSecret: String,
) : LoadProviderResourceTokenPort {
	override fun getToken(authorizationToken: String): String {
		val token: GoogleAuthorizationTokenResponse =
			authorizationClient
				.getAuthorizationToken(
					authorizationToken,
					grantType,
					clientId,
					redirectUri,
					clientSecret,
				)

		return "${token.tokenType} ${token.accessToken}"
	}
}