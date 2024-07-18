package kr.kro.dokbaro.server.domain.auth.adapter.output.kakao

import kr.kro.dokbaro.server.domain.auth.adapter.output.kakao.external.KakaoResourceClient
import kr.kro.dokbaro.server.domain.auth.adapter.output.kakao.external.resource.KakaoAccount
import kr.kro.dokbaro.server.domain.auth.model.ProviderAccount
import kr.kro.dokbaro.server.domain.auth.port.output.LoadProviderAccountPort
import kr.kro.dokbaro.server.global.AuthProvider
import org.springframework.stereotype.Component

@Component
class KakaoAccountLoader(
	private val resourceClient: KakaoResourceClient,
) : LoadProviderAccountPort {
	override fun getAttributes(accessToken: String): ProviderAccount {
		val account: KakaoAccount = resourceClient.getUserProfiles(accessToken)

		return ProviderAccount(
			AuthProvider.KAKAO,
			account.id.toString(),
			account.kakaoAccount.name,
			account.kakaoAccount.email,
			account.kakaoAccount.profile.profileImageUrl,
		)
	}
}