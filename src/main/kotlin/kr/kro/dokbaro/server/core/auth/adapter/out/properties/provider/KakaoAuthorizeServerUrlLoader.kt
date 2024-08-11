package kr.kro.dokbaro.server.core.auth.adapter.out.properties.provider

import kr.kro.dokbaro.server.core.auth.adapter.out.properties.OAuth2AuthorizeServerUrlLoader
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.util.UriComponentsBuilder

@Component
class KakaoAuthorizeServerUrlLoader(
	@Value("\${oauth2.kakao.provider.authorization.url}") private val authorizationUrl: String,
	@Value("\${oauth2.kakao.provider.authorization.redirect-path}") private val providerRedirectPath: String,
	@Value("\${oauth2.kakao.client.id}") private val clientId: String,
	@Value("\${oauth2.kakao.client.scope}") private val scope: String,
) : OAuth2AuthorizeServerUrlLoader {
	override fun getUrl(redirectUrl: String): String =
		UriComponentsBuilder
			.fromUriString(authorizationUrl + providerRedirectPath)
			.queryParam("response_type", "code")
			.queryParam("client_id", clientId)
			.queryParam("redirect_uri", redirectUrl)
			.queryParam("scope", scope)
			.toUriString()
}