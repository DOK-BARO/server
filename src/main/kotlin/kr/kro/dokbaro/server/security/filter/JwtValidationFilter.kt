package kr.kro.dokbaro.server.security.filter

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import kr.kro.dokbaro.server.security.SecurityConstants
import kr.kro.dokbaro.server.security.authentication.JwtUnauthenticatedToken
import kr.kro.dokbaro.server.security.jwt.setUpJwtCookie
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter

class JwtValidationFilter(
	private val authenticationManager: AuthenticationManager,
) : OncePerRequestFilter() {
	override fun doFilterInternal(
		request: HttpServletRequest,
		response: HttpServletResponse,
		filterChain: FilterChain,
	) {
		var accessToken: String? = request.cookies.find { it.name == SecurityConstants.AUTHORIZATION }?.value
		val refreshToken: String? = request.cookies.find { it.name == SecurityConstants.REFRESH }?.value

		if (refreshToken != null) {
			TODO()

			accessToken = TODO()

			response.setUpJwtCookie(TODO())
		}

		TODO("extract accessToken")
		SecurityContextHolder.getContext().authentication =
			authenticationManager.authenticate(JwtUnauthenticatedToken(TODO()))

		filterChain.doFilter(request, response)
	}

	override fun shouldNotFilter(request: HttpServletRequest): Boolean =
		request.cookies.none {
			it.name == SecurityConstants.AUTHORIZATION || it.name == SecurityConstants.REFRESH
		}
}