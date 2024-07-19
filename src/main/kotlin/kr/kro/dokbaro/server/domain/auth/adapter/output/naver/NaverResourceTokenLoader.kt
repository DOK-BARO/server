package kr.kro.dokbaro.server.domain.auth.adapter.output.naver

import kr.kro.dokbaro.server.domain.auth.adapter.output.naver.external.NaverAuthorizationClient
import kr.kro.dokbaro.server.domain.auth.adapter.output.naver.external.NaverAuthorizationTokenResponse
import kr.kro.dokbaro.server.domain.auth.port.output.LoadProviderResourceTokenPort
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class NaverResourceTokenLoader(
	private val authorizationClient: NaverAuthorizationClient,
	@Value("\${oauth2.naver.provider.grant-type}") private val grantType: String,
	@Value("\${oauth2.naver.client.id}") private val clientId: String,
	@Value("\${oauth2.naver.client.secret}") private val clientSecret: String,
) : LoadProviderResourceTokenPort {
	override fun getToken(
		authorizationToken: String,
		redirectUrl: String,
	): String {
		val token: NaverAuthorizationTokenResponse =
			authorizationClient
				.getAuthorizationToken(
					authorizationToken,
					grantType,
					clientId,
					redirectUrl,
					clientSecret,
				)

		return "${token.tokenType} ${token.accessToken}"
	}
}