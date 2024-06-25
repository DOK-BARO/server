package kr.kro.dokbaro.server.configuration.security.oauth2.provider

import kr.kro.dokbaro.server.configuration.security.oauth2.provider.mapper.GoogleAttributeMapper
import kr.kro.dokbaro.server.configuration.security.oauth2.provider.mapper.KakaoAttributeMapper

enum class OAuthProvider(
	private val attributeMapper: AttributeMapper,
) {
	GOOGLE(GoogleAttributeMapper()),
	KAKAO(KakaoAttributeMapper()),
	;

	fun map(attributes: Map<String, Any>): OAuthProviderResponse = attributeMapper.map(attributes)
}