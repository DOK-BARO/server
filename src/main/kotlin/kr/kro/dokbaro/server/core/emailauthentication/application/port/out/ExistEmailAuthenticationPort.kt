package kr.kro.dokbaro.server.core.emailauthentication.application.port.out

import kr.kro.dokbaro.server.core.emailauthentication.application.port.out.dto.SearchEmailAuthenticationCondition

fun interface ExistEmailAuthenticationPort {
	fun existBy(condition: SearchEmailAuthenticationCondition): Boolean
}