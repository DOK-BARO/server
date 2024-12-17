package kr.kro.dokbaro.server.security.filter

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import kr.kro.dokbaro.server.security.SecurityConstants
import kr.kro.dokbaro.server.security.authentication.JwtUnauthenticatedToken
import kr.kro.dokbaro.server.security.jwt.JwtResponse
import kr.kro.dokbaro.server.security.jwt.JwtTokenReGenerator
import kr.kro.dokbaro.server.security.jwt.cookie.JwtHttpCookieInjector
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter

class JwtValidationFilter(
	private val authenticationManager: AuthenticationManager,
	private val jwtTokenReGenerator: JwtTokenReGenerator,
	private val jwtHttpCookieInjector: JwtHttpCookieInjector,
) : OncePerRequestFilter() {
	override fun doFilterInternal(
		request: HttpServletRequest,
		response: HttpServletResponse,
		filterChain: FilterChain,
	) {
		var accessToken: String? = request.cookies.find { it.name == SecurityConstants.AUTHORIZATION }?.value
		val refreshToken: String? = request.cookies.find { it.name == SecurityConstants.REFRESH }?.value

		try {
			if (accessToken == null && refreshToken != null) {
				val newToken: JwtResponse = jwtTokenReGenerator.reGenerate(refreshToken)

				accessToken = newToken.accessToken

				jwtHttpCookieInjector.inject(response, newToken)
			}

			SecurityContextHolder.getContext().authentication =
				authenticationManager.authenticate(JwtUnauthenticatedToken(accessToken!!))
		} catch (e: AuthenticationException) {
			response.status = HttpServletResponse.SC_UNAUTHORIZED
		}

		filterChain.doFilter(request, response)
	}

	override fun shouldNotFilter(request: HttpServletRequest): Boolean =
		request.cookies == null ||
			request.cookies.none {
				it.name == SecurityConstants.AUTHORIZATION || it.name == SecurityConstants.REFRESH
			}
}