package kr.kro.dokbaro.server.security.oauth2

import kr.kro.dokbaro.server.core.account.domain.AuthProvider
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.EnumMap

@Configuration
class OAuth2Config {
	@Bean
	fun socialUserMappers(mappers: Collection<SocialUserMapper>): EnumMap<AuthProvider, SocialUserMapper> =
		EnumMap(mappers.associateBy { it.provider() })
}