package kr.kro.dokbaro.server.domain.auth.adapter.output.kakao

import kr.kro.dokbaro.server.domain.auth.adapter.output.kakao.external.KakaoResourceClient
import kr.kro.dokbaro.server.domain.auth.adapter.output.kakao.external.provideraccount.KakaoAccount
import kr.kro.dokbaro.server.domain.auth.model.ProviderAccount
import kr.kro.dokbaro.server.domain.auth.port.output.LoadProviderAccountPort
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class KakaoAccountLoader(
	private val resourceClient: KakaoResourceClient,
	@Value("\${oauth2.kakao.provider.grant-type}") grantType: String,
	@Value("\${oauth2.kakao.client.id}") clientId: String,
	@Value("\${oauth2.kakao.client.redirect-uri}") redirectUri: String,
	@Value("\${oauth2.kakao.client.secret}") clientSecret: String,
) : LoadProviderAccountPort {
	override fun getAttributes(accessToken: String): ProviderAccount {
		val account: KakaoAccount = resourceClient.getUserProfiles(accessToken)

		return ProviderAccount(
			"kakao",
			account.id.toString(),
			account.kakaoAccount.name,
			account.kakaoAccount.email,
			account.kakaoAccount.profile.profileImageUrl,
		)
	}
}