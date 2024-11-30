package kr.kro.dokbaro.server.core.auth.oauth2.adapter.out.web.naver

import kr.kro.dokbaro.server.core.auth.oauth2.adapter.out.web.ProviderResourceTokenLoader
import kr.kro.dokbaro.server.core.auth.oauth2.adapter.out.web.naver.external.NaverAuthorizationClient
import kr.kro.dokbaro.server.core.auth.oauth2.adapter.out.web.naver.external.NaverAuthorizationTokenResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class NaverResourceTokenLoader(
	private val authorizationClient: NaverAuthorizationClient,
	@Value("\${oauth2.naver.provider.grant-type}") private val grantType: String,
	@Value("\${oauth2.naver.client.id}") private val clientId: String,
	@Value("\${oauth2.naver.client.secret}") private val clientSecret: String,
) : ProviderResourceTokenLoader {
	override fun getResource(
		authorizationToken: String,
		redirectUrl: String,
	): String {
		val token: NaverAuthorizationTokenResponse =
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