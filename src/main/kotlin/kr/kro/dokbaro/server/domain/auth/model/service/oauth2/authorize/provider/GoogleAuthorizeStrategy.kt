package kr.kro.dokbaro.server.domain.auth.model.service.oauth2.authorize.provider

import kr.kro.dokbaro.server.domain.auth.model.service.oauth2.authorize.OAuth2AuthorizeStrategy
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.util.UriComponentsBuilder

@Component
class GoogleAuthorizeStrategy(
	@Value("\${oauth2.google.provider.authorization-url}") private val authorizationUrl: String,
	@Value("\${oauth2.google.client.redirect-uri}") private val clientRedirectUri: String,
	@Value("\${oauth2.google.client.scope}") private val scope: String,
	@Value("\${oauth2.google.client.id}") private val clientId: String,
) : OAuth2AuthorizeStrategy {
	override fun getUri(): String =
		UriComponentsBuilder
			.fromUriString(authorizationUrl)
			.queryParam("response_type", "code")
			.queryParam("client_id", clientId)
			.queryParam("redirect_uri", clientRedirectUri)
			.queryParam("scope", scope)
			.toUriString()
}