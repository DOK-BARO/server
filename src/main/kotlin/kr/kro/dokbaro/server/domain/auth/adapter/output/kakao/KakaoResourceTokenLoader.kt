package kr.kro.dokbaro.server.domain.auth.adapter.output.kakao

import kr.kro.dokbaro.server.domain.auth.adapter.output.kakao.external.KakaoAuthorizationClient
import kr.kro.dokbaro.server.domain.auth.adapter.output.kakao.external.KakaoAuthorizationTokenResponse
import kr.kro.dokbaro.server.domain.auth.port.output.LoadProviderResourceTokenPort
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class KakaoResourceTokenLoader(
	private val authorizationClient: KakaoAuthorizationClient,
	@Value("\${oauth2.kakao.provider.grant-type}") private val grantType: String,
	@Value("\${oauth2.kakao.client.id}") private val clientId: String,
	@Value("\${oauth2.kakao.client.secret}") private val clientSecret: String,
) : LoadProviderResourceTokenPort {
	override fun getToken(
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