package kr.kro.dokbaro.server.configuration.security.oauth2

import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import kr.kro.dokbaro.server.configuration.security.token.AuthTokenGenerator
import kr.kro.dokbaro.server.configuration.security.token.AuthTokens
import kr.kro.dokbaro.server.configuration.security.token.TokenClaims
import kr.kro.dokbaro.server.domain.account.port.input.query.FindOneAccountQuery
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.AbstractAuthenticationTargetUrlRequestHandler
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.stereotype.Component

@Component
class OAuth2SuccessHandler(
	private val findOneAccountQuery: FindOneAccountQuery,
	private val authTokenGenerator: AuthTokenGenerator,
	@Value("\${login.success.redirect-url}") val redirectUrl: String,
	@Value("\${login.cookie.access-token-key}") val accessTokenKey: String,
	@Value("\${login.cookie.refresh-token-key}") val refreshTokenKey: String,
) : AbstractAuthenticationTargetUrlRequestHandler(),
	AuthenticationSuccessHandler {
	override fun onAuthenticationSuccess(
		request: HttpServletRequest,
		response: HttpServletResponse,
		authentication: Authentication,
	) {
		val authMember = findOneAccountQuery.getBy(authentication.name)
		val token: AuthTokens = authTokenGenerator.generate(TokenClaims(authMember.id.toString(), authMember.role))
		response.addCookie(compactCookie(accessTokenKey, token.accessToken))
		response.addCookie(compactCookie(refreshTokenKey, token.refreshToken))
		redirectStrategy.sendRedirect(request, response, redirectUrl)
	}

	private fun compactCookie(
		key: String,
		value: String,
	): Cookie {
		val cookie = Cookie(key, value)
		cookie.path = "/"
		cookie.isHttpOnly = true
		cookie.secure = true
		return cookie
	}
}