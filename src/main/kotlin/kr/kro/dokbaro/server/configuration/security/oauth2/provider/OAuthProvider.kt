package kr.kro.dokbaro.server.configuration.security.oauth2.provider

import kr.kro.dokbaro.server.configuration.security.oauth2.provider.mapper.GithubAttributeMapper
import kr.kro.dokbaro.server.configuration.security.oauth2.provider.mapper.GoogleAttributeMapper
import kr.kro.dokbaro.server.configuration.security.oauth2.provider.mapper.KakaoAttributeMapper
import kr.kro.dokbaro.server.configuration.security.oauth2.provider.mapper.NaverAttributeMapper

enum class OAuthProvider(
	private val attributeMapper: AttributeMapper,
) {
	GOOGLE(GoogleAttributeMapper()),
	KAKAO(KakaoAttributeMapper()),
	NAVER(NaverAttributeMapper()),
	GITHUB(GithubAttributeMapper()),
	;

	fun map(attributes: Map<String, Any>): OAuthProviderResponse = attributeMapper.map(attributes)
}