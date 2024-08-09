package kr.kro.dokbaro.server.core.auth.adapter.out.properties.provider

import kr.kro.dokbaro.server.core.auth.adapter.out.properties.OAuth2AuthorizeServerUrlLoader
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.util.UriComponentsBuilder

@Component
class NaverAuthorizeServerUrlLoader(
	@Value("\${oauth2.naver.provider.authorization.url}") private val authorizationUrl: String,
	@Value("\${oauth2.naver.provider.authorization.redirect-path}") private val providerRedirectPath: String,
	@Value("\${oauth2.naver.client.redirect-uri}") private val clientRedirectUri: String,
	@Value("\${oauth2.naver.client.id}") private val clientId: String,
) : OAuth2AuthorizeServerUrlLoader {
	override fun get(): String =
		UriComponentsBuilder
			.fromUriString(authorizationUrl + providerRedirectPath)
			.queryParam("response_type", "code")
			.queryParam("client_id", clientId)
			.queryParam("redirect_uri", clientRedirectUri)
			.toUriString()
}