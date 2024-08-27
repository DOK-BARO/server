package kr.kro.dokbaro.server.core.auth.oauth2.adapter.out.web.kakao

import kr.kro.dokbaro.server.core.auth.oauth2.adapter.out.web.ProviderResourceTokenLoader
import kr.kro.dokbaro.server.core.auth.oauth2.adapter.out.web.kakao.external.KakaoAuthorizationClient
import kr.kro.dokbaro.server.core.auth.oauth2.adapter.out.web.kakao.external.KakaoAuthorizationTokenResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class KakaoResourceTokenLoader(
	private val authorizationClient: KakaoAuthorizationClient,
	@Value("\${oauth2.kakao.provider.grant-type}") private val grantType: String,
	@Value("\${oauth2.kakao.client.id}") private val clientId: String,
	@Value("\${oauth2.kakao.client.secret}") private val clientSecret: String,
) : ProviderResourceTokenLoader {
	override fun get(
		authorizationToken: String,
		redirectUrl: String,
	): String {
		val token: KakaoAuthorizationTokenResponse =
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