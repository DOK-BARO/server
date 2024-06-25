package kr.kro.dokbaro.server.configuration.security.token

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import kr.kro.dokbaro.server.configuration.security.AuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter

class TokenBasedAuthorizationFilter(
	private val tokenExtractor: TokenExtractor,
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
			?.filter { it.name == "Authorization" }
			?.map { it.value }
			?.firstOrNull()
}