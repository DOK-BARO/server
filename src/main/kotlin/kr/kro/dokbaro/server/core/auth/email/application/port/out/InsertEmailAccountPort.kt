package kr.kro.dokbaro.server.core.auth.email.application.port.out

import kr.kro.dokbaro.server.core.auth.email.domain.AccountPassword

fun interface InsertEmailAccountPort {
	fun insert(accountPassword: AccountPassword): Long
}