package kr.kro.dokbaro.server.core.security

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import kr.kro.dokbaro.server.common.http.jwt.JwtCookiePairGenerator
import kr.kro.dokbaro.server.core.token.application.port.input.DecodeAccessTokenUseCase
import kr.kro.dokbaro.server.core.token.application.port.input.ReGenerateAuthTokenUseCase
import kr.kro.dokbaro.server.core.token.domain.access.jwt.TokenClaims
import org.springframework.http.HttpHeaders
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter

class TokenBasedAuthorizationFilter(
	private val decodeAccessTokenUseCase: DecodeAccessTokenUseCase,
	private val accessTokenKey: String,
	private val refreshTokenKey: String,
	private val reGenerateAuthTokenUseCase: ReGenerateAuthTokenUseCase,
	private val jwtCookiePairGenerator: JwtCookiePairGenerator,
) : OncePerRequestFilter() {
	override fun doFilterInternal(
		request: HttpServletRequest,
		response: HttpServletResponse,
		filterChain: FilterChain,
	) {
		var accessToken: String? = getAccessToken(request)
		val refreshToken: String? = getRefreshToken(request)

		if (accessToken == null && refreshToken != null) {
			val (newAccess: String, newRefresh: String) = reGenerateAuthTokenUseCase.reGenerate(refreshToken)

			val (accessCookie: String, refreshCookie: String) = jwtCookiePairGenerator.getJwtCookiePair(newAccess, newRefresh)
			response.setHeader(HttpHeaders.SET_COOKIE, accessCookie)
			response.setHeader(HttpHeaders.SET_COOKIE, refreshCookie)

			accessToken = newAccess
		}

		accessToken?.run {
			val claims: TokenClaims = decodeAccessTokenUseCase.decode(this)
			SecurityContextHolder.getContext().authentication =
				AuthenticationToken(claims.id, claims.role)
		}

		filterChain.doFilter(request, response)
	}

	private fun getAccessToken(request: HttpServletRequest): String? =
		request.cookies
			?.filter { it.name == accessTokenKey }
			?.map { it.value }
			?.firstOrNull()

	private fun getRefreshToken(request: HttpServletRequest): String? =
		request.cookies
			?.filter { it.name == refreshTokenKey }
			?.map { it.value }
			?.firstOrNull()
}