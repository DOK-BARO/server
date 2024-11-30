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
	override fun getAccount(accessToken: String): OAuth2ProviderAccount {
		val account: KakaoAccount = resourceClient.getUserProfiles(accessToken)

		return OAuth2ProviderAccount(
			provider = AuthProvider.KAKAO,
			id = account.id.toString(),
			name = account.kakaoAccount.profile.nickname ?: throw NickNameNotExistException(),
			email = account.kakaoAccount.email ?: throw EmailNotExistException(),
			profileImage = account.kakaoAccount.profile.profileImageUrl,
		)
	}
}