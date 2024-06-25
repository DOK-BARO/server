package kr.kro.dokbaro.server.configuration.security.oauth2.provider.mapper

import kr.kro.dokbaro.server.configuration.security.oauth2.provider.AttributeMapper
import kr.kro.dokbaro.server.configuration.security.oauth2.provider.OAuthProviderResponse

class KakaoAttributeMapper : AttributeMapper {
	override fun map(attributes: Map<String, Any>): OAuthProviderResponse {
		val accountId = attributes["id"].toString()

		return OAuthProviderResponse(accountId)
	}
}