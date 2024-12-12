package kr.kro.dokbaro.server.core.account.application.port.out

import kr.kro.dokbaro.server.core.account.domain.AccountPassword

fun interface UpdateAccountPasswordPort {
	fun updateAccountPassword(accountPassword: AccountPassword)
}