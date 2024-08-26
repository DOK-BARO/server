package kr.kro.dokbaro.server.core.auth.oauth2.adapter.out.web.kakao

import kr.kro.dokbaro.server.common.type.AuthProvider
import kr.kro.dokbaro.server.core.auth.oauth2.adapter.out.web.EmailNotExistException
import kr.kro.dokbaro.server.core.auth.oauth2.adapter.out.web.NickNameNotExistException
import kr.kro.dokbaro.server.core.auth.oauth2.adapter.out.web.ProviderAccountLoader
import kr.kro.dokbaro.server.core.auth.oauth2.adapter.out.web.kakao.external.KakaoResourceClient
import kr.kro.dokbaro.server.core.auth.oauth2.adapter.out.web.kakao.external.resource.KakaoAccount
import kr.kro.dokbaro.server.core.auth.oauth2.application.port.dto.OAuth2ProviderAccount
import org.springframework.stereotype.Component

@Component
class KakaoAccountLoader(
	private val resourceClient: KakaoResourceClient,
) : ProviderAccountLoader {
	override fun get(accessToken: String): OAuth2ProviderAccount {
		val account: KakaoAccount = resourceClient.getUserProfiles(accessToken)

		return OAuth2ProviderAccount(
			AuthProvider.KAKAO,
			account.id.toString(),
			account.kakaoAccount.name ?: throw NickNameNotExistException(),
			account.kakaoAccount.email ?: throw EmailNotExistException(),
			account.kakaoAccount.profile.profileImageUrl,
		)
	}
}