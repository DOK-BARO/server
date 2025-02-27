package kr.kro.dokbaro.server.security.oauth2

import kr.kro.dokbaro.server.core.account.domain.AuthProvider
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal
import org.springframework.stereotype.Component

@Component
class GoogleAttributeMapper : SocialUserMapper {
	@Suppress("UNCHECKED_CAST")
	override fun toSocialUser(oAuth2User: OAuth2AuthenticatedPrincipal): SocialUser {
		val attributes = oAuth2User.attributes

		return SocialUser(
			id = attributes["sub"].toString(),
			email = attributes["email"].toString(),
			nickname = attributes["name"].toString(),
			provider = provider(),
			profileImage = attributes["picture"].toString(),
		)
	}

	override fun provider(): AuthProvider = AuthProvider.GOOGLE
}