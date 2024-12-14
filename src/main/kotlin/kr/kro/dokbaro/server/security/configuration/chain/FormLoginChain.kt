package kr.kro.dokbaro.server.security.configuration.chain

import kr.kro.dokbaro.server.security.annotation.HttpSecurityChain
import kr.kro.dokbaro.server.security.configuration.SecurityChain
import kr.kro.dokbaro.server.security.handler.FormAuthenticationFailureHandler
import kr.kro.dokbaro.server.security.handler.FormAuthenticationSuccessHandler
import org.springframework.security.config.annotation.web.builders.HttpSecurity

@HttpSecurityChain
class FormLoginChain(
	private val formAuthenticationFailureHandler: FormAuthenticationFailureHandler,
	private val formAuthenticationSuccessHandler: FormAuthenticationSuccessHandler,
) : SecurityChain {
	override fun link(origin: HttpSecurity): HttpSecurity =
		origin.formLogin {
			it.loginProcessingUrl("/auth/login/email")
			it.usernameParameter("email")
			it.passwordParameter("password")
			it.successHandler(formAuthenticationSuccessHandler)
			it.failureHandler(formAuthenticationFailureHandler)
		}
}