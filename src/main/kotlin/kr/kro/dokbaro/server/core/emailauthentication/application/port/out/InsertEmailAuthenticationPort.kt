package kr.kro.dokbaro.server.core.emailauthentication.application.port.out

import kr.kro.dokbaro.server.core.emailauthentication.domain.EmailAuthentication

fun interface InsertEmailAuthenticationPort {
	fun insert(emailAuthentication: EmailAuthentication): Long
}