package kr.kro.dokbaro.server.security.configuration.chain

import kr.kro.dokbaro.server.security.annotation.HttpSecurityChain
import kr.kro.dokbaro.server.security.configuration.SecurityChain
import kr.kro.dokbaro.server.security.configuration.exception.AuthenticationFailureEntryPoint
import kr.kro.dokbaro.server.security.configuration.exception.CustomAccessDeniedHandler
import org.springframework.security.config.annotation.web.builders.HttpSecurity

@HttpSecurityChain
class ExceptionHandlingChain(
	private val authenticationFailureEntryPoint: AuthenticationFailureEntryPoint,
	private val accessDeniedHandler: CustomAccessDeniedHandler,
) : SecurityChain {
	override fun link(origin: HttpSecurity): HttpSecurity =
		origin.exceptionHandling {
			it.accessDeniedHandler(accessDeniedHandler)
			it.authenticationEntryPoint(authenticationFailureEntryPoint)
		}
}