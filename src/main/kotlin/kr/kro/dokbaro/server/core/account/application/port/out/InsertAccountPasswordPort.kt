package kr.kro.dokbaro.server.core.account.application.port.out

import kr.kro.dokbaro.server.core.account.domain.AccountPassword

fun interface InsertAccountPasswordPort {
	fun insertAccountPassword(accountPassword: AccountPassword)
}