package kr.kro.dokbaro.server.security.configuration.chain

import kr.kro.dokbaro.server.security.annotation.HttpSecurityChain
import kr.kro.dokbaro.server.security.configuration.SecurityChain
import kr.kro.dokbaro.server.security.handler.CustomLogoutHandler
import kr.kro.dokbaro.server.security.handler.CustomLogoutSuccessHandler
import org.springframework.security.config.annotation.web.builders.HttpSecurity

@HttpSecurityChain
class LogoutChain(
	private val logoutHandler: CustomLogoutHandler,
	private val logoutSuccessHandler: CustomLogoutSuccessHandler,
) : SecurityChain {
	override fun link(origin: HttpSecurity): HttpSecurity =
		origin.logout {
			it.logoutUrl("/auth/logout")
			it.logoutHandlers.add(logoutHandler)
			it.logoutSuccessHandler(logoutSuccessHandler)
		}
}