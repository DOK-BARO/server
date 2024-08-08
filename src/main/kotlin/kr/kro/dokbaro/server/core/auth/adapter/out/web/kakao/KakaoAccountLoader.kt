package kr.kro.dokbaro.server.core.auth.adapter.out.web.kakao

import kr.kro.dokbaro.server.core.auth.adapter.out.web.kakao.external.KakaoResourceClient
import kr.kro.dokbaro.server.core.auth.adapter.out.web.kakao.external.resource.KakaoAccount
import kr.kro.dokbaro.server.core.auth.application.port.out.LoadProviderAccountPort
import kr.kro.dokbaro.server.core.auth.domain.ProviderAccount
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