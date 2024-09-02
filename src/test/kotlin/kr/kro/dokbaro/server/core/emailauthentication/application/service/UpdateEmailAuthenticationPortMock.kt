package kr.kro.dokbaro.server.core.emailauthentication.application.service

import kr.kro.dokbaro.server.core.emailauthentication.application.port.out.UpdateEmailAuthenticationPort
import kr.kro.dokbaro.server.core.emailauthentication.domain.EmailAuthentication

class UpdateEmailAuthenticationPortMock(
	var storage: EmailAuthentication? = null,
) : UpdateEmailAuthenticationPort {
	override fun update(emailAuthentication: EmailAuthentication) {
		storage = emailAuthentication
	}

	fun clear() {
		storage = null
	}
}