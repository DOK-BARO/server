package kr.kro.dokbaro.server.configuration.security.oauth2.provider.mapper

import kr.kro.dokbaro.server.configuration.security.oauth2.provider.AttributeMapper
import kr.kro.dokbaro.server.configuration.security.oauth2.provider.OAuthProviderResponse

class NaverAttributeMapper : AttributeMapper {
	override fun map(attributes: Map<String, Any>): OAuthProviderResponse {
		val response = attributes["response"] as Map<*, *>
		val accountId = response["id"].toString()

		return OAuthProviderResponse(accountId)
	}
}