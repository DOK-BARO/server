package kr.kro.dokbaro.server.core.emailauthentication.application.port.out

import kr.kro.dokbaro.server.core.emailauthentication.domain.EmailAuthentication

fun interface UpdateEmailAuthenticationPort {
	fun update(emailAuthentication: EmailAuthentication)
}