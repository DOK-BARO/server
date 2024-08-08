package kr.kro.dokbaro.server.core.auth.application.service.oauth2.authorize.provider

import kr.kro.dokbaro.server.core.auth.application.service.oauth2.authorize.OAuth2AuthorizeStrategy
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.util.UriComponentsBuilder

@Component
class KakaoAuthorizeStrategy(
	@Value("\${oauth2.kakao.provider.authorization.url}") private val authorizationUrl: String,
	@Value("\${oauth2.kakao.provider.authorization.redirect-path}") private val providerRedirectPath: String,
	@Value("\${oauth2.kakao.client.redirect-uri}") private val clientRedirectUri: String,
	@Value("\${oauth2.kakao.client.id}") private val clientId: String,
	@Value("\${oauth2.kakao.client.scope}") private val scope: String,
) : OAuth2AuthorizeStrategy {
	override fun getUri(): String =
		UriComponentsBuilder
			.fromUriString(authorizationUrl + providerRedirectPath)
			.queryParam("response_type", "code")
			.queryParam("client_id", clientId)
			.queryParam("redirect_uri", clientRedirectUri)
			.queryParam("scope", scope)
			.toUriString()
}