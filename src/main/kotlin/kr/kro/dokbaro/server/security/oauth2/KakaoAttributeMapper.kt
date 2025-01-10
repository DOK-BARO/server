package kr.kro.dokbaro.server.security.oauth2

import kr.kro.dokbaro.server.core.account.domain.AuthProvider
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal
import org.springframework.stereotype.Component

@Component
@Suppress("UNCHECKED_CAST")
class KakaoAttributeMapper : SocialUserMapper {
	@Suppress("UNCHECKED_CAST")
	override fun toSocialUser(oAuth2User: OAuth2AuthenticatedPrincipal): SocialUser {
		val properties = oAuth2User.attributes["properties"] as Map<String, Any>
		val kakaoAccount = oAuth2User.attributes["kakao_account"] as Map<String, Any>

		return SocialUser(
			id = oAuth2User.attributes["id"].toString(),
			email = kakaoAccount["email"]?.toString(),
			nickname = properties["nickname"].toString(),
			provider = provider(),
			profileImage = properties["profile_image"].toString(),
		)
	}

	override fun provider(): AuthProvider = AuthProvider.KAKAO
}