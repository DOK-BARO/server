package kr.kro.dokbaro.server.core.auth.adapter.out.web.kakao

import kr.kro.dokbaro.server.common.type.AuthProvider
import kr.kro.dokbaro.server.core.auth.adapter.out.web.ProviderAccountLoader
import kr.kro.dokbaro.server.core.auth.adapter.out.web.kakao.external.KakaoResourceClient
import kr.kro.dokbaro.server.core.auth.adapter.out.web.kakao.external.resource.KakaoAccount
import kr.kro.dokbaro.server.core.auth.domain.ProviderAccount
import org.springframework.stereotype.Component

@Component
class KakaoAccountLoader(
	private val resourceClient: KakaoResourceClient,
) : ProviderAccountLoader {
	override fun get(accessToken: String): ProviderAccount {
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