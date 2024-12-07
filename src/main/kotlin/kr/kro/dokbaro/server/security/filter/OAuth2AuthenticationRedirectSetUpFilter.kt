package kr.kro.dokbaro.server.security.filter

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import kr.kro.dokbaro.server.security.SecurityConstants
import org.springframework.web.filter.OncePerRequestFilter

class OAuth2AuthenticationRedirectSetUpFilter : OncePerRequestFilter() {
	override fun doFilterInternal(
		request: HttpServletRequest,
		response: HttpServletResponse,
		filterChain: FilterChain,
	) {
		request.session.setAttribute(
			SecurityConstants.CLIENT_REDIRECT_URL,
			request.getParameter(SecurityConstants.REDIRECT_URL),
		)
		filterChain.doFilter(request, response)
	}

	override fun shouldNotFilter(request: HttpServletRequest): Boolean =
		!request.servletPath.startsWith("/auth/login/oauth2")
}