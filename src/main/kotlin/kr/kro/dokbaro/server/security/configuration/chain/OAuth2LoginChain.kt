package kr.kro.dokbaro.server.security.configuration.chain

import kr.kro.dokbaro.server.security.annotation.HttpSecurityChain
import kr.kro.dokbaro.server.security.configuration.SecurityChain
import kr.kro.dokbaro.server.security.handler.OAuth2AuthenticationSuccessHandler
import org.springframework.security.config.annotation.web.builders.HttpSecurity

@HttpSecurityChain
class OAuth2LoginChain(
	private val oAuth2AuthenticationSuccessHandler: OAuth2AuthenticationSuccessHandler,
) : SecurityChain {
	override fun link(origin: HttpSecurity): HttpSecurity =
		origin.oauth2Login { l ->
			l.authorizationEndpoint { endpoint ->
				endpoint.baseUri("/auth/login/oauth2")
			}
			l.successHandler(oAuth2AuthenticationSuccessHandler)
		}
}