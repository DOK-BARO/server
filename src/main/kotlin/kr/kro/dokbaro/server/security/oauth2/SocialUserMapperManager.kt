package kr.kro.dokbaro.server.security.oauth2

import kr.kro.dokbaro.server.core.account.domain.AuthProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import org.springframework.stereotype.Component
import java.util.EnumMap

@Component
class SocialUserMapperManager(
	val mappers: EnumMap<AuthProvider, SocialUserMapper>,
) {
	@Autowired
	constructor(mappers: Collection<SocialUserMapper>) : this(EnumMap(mappers.associateBy { it.provider() }))

	fun toSocialUser(oAuth2User: OAuth2AuthenticationToken): SocialUser {
		val provider: AuthProvider = AuthProvider.valueOf(oAuth2User.authorizedClientRegistrationId.uppercase())

		return mappers[provider]!!.toSocialUser(oAuth2User.principal)
	}
}