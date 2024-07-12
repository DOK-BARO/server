package kr.kro.dokbaro.server.domain.auth.model.service.oauth2.redirected.provider

import kr.kro.dokbaro.server.domain.auth.model.service.oauth2.redirected.OAuth2RedirectStrategy
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.util.UriComponentsBuilder

@Component
class KakaoRedirectStrategy(
	@Value("\${oauth2.kakao.provider.authorization.url}") private val authorizationUrl: String,
	@Value("\${oauth2.kakao.provider.authorization.redirect-path}") private val providerRedirectPath: String,
	@Value("\${oauth2.kakao.client.redirect-uri}") private val clientRedirectUri: String,
	@Value("\${oauth2.kakao.client.id}") private val clientId: String,
	@Value("\${oauth2.kakao.client.scope}") private val scope: String,
) : OAuth2RedirectStrategy {
	override fun getUri(): String =
		UriComponentsBuilder
			.fromUriString(authorizationUrl + providerRedirectPath)
			.queryParam("response_type", "code")
			.queryParam("client_id", clientId)
			.queryParam("redirect_uri", clientRedirectUri)
			.queryParam("scope", scope)
			.toUriString()
}