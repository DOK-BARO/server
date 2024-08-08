package kr.kro.dokbaro.server.core.auth.adapter.out.web.naver

import kr.kro.dokbaro.server.core.auth.adapter.out.web.naver.external.NaverAuthorizationClient
import kr.kro.dokbaro.server.core.auth.adapter.out.web.naver.external.NaverAuthorizationTokenResponse
import kr.kro.dokbaro.server.core.auth.application.port.out.LoadProviderResourceTokenPort
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