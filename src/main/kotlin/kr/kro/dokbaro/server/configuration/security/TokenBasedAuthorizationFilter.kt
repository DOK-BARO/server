package kr.kro.dokbaro.server.configuration.security

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import kr.kro.dokbaro.server.domain.token.TokenClaims
import kr.kro.dokbaro.server.domain.token.TokenExtractor
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter

class TokenBasedAuthorizationFilter(
	private val tokenExtractor: TokenExtractor,
	private val accessTokenKey: String,
) : OncePerRequestFilter() {
	override fun doFilterInternal(
		request: HttpServletRequest,
		response: HttpServletResponse,
		filterChain: FilterChain,
	) {
		getAccessToken(request)?.run {
			val claims: TokenClaims = tokenExtractor.extract(this)
			SecurityContextHolder.getContext().authentication =
				AuthenticationToken(claims.id, claims.role)
		}

		filterChain.doFilter(request, response)
	}

	private fun getAccessToken(request: HttpServletRequest) =
		request.cookies
			?.filter { it.name == accessTokenKey }
			?.map { it.value }
			?.firstOrNull()
}