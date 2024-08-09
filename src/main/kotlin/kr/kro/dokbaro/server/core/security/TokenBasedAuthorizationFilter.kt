package kr.kro.dokbaro.server.core.security

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import kr.kro.dokbaro.server.core.token.application.port.input.DecodeAccessTokenUseCase
import kr.kro.dokbaro.server.core.token.domain.access.jwt.TokenClaims
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter

class TokenBasedAuthorizationFilter(
	private val tokenExtractor: DecodeAccessTokenUseCase,
	private val accessTokenKey: String,
) : OncePerRequestFilter() {
	override fun doFilterInternal(
		request: HttpServletRequest,
		response: HttpServletResponse,
		filterChain: FilterChain,
	) {
		getAccessToken(request)?.run {
			val claims: TokenClaims = tokenExtractor.decode(this)
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
}