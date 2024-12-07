package kr.kro.dokbaro.server.security.oauth2

import kr.kro.dokbaro.server.core.account.domain.AuthProvider
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal

class NaverAttributeMapper : SocialUserMapper {
	override fun toSocialUser(oAuth2User: OAuth2AuthenticatedPrincipal): SocialUser {
		TODO("Not yet implemented")
	}

	override fun provider(): AuthProvider = AuthProvider.NAVER
}