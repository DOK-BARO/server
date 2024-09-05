package kr.kro.dokbaro.server.core.auth.email.application.port.out

import kr.kro.dokbaro.server.core.auth.email.domain.AccountPassword

fun interface SaveEmailAccountPort {
	fun save(accountPassword: AccountPassword): Long
}