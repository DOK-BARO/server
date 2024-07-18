package kr.kro.dokbaro.server.domain.auth.model.service.oauth2.authorize.provider

import kr.kro.dokbaro.server.domain.auth.model.service.oauth2.authorize.OAuth2AuthorizeStrategy
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.util.UriComponentsBuilder

@Component
class NaverAuthorizeStrategy(
	@Value("\${oauth2.naver.provider.authorization.url}") private val authorizationUrl: String,
	@Value("\${oauth2.naver.provider.authorization.redirect-path}") private val providerRedirectPath: String,
	@Value("\${oauth2.naver.client.redirect-uri}") private val clientRedirectUri: String,
	@Value("\${oauth2.naver.client.id}") private val clientId: String,
) : OAuth2AuthorizeStrategy {
	override fun getUri(): String =
		UriComponentsBuilder
			.fromUriString(authorizationUrl + providerRedirectPath)
			.queryParam("response_type", "code")
			.queryParam("client_id", clientId)
			.queryParam("redirect_uri", clientRedirectUri)
			.toUriString()
}