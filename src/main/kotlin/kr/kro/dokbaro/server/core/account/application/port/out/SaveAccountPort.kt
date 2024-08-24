package kr.kro.dokbaro.server.core.account.application.port.out

import kr.kro.dokbaro.server.core.account.domain.Account

fun interface SaveAccountPort {
	fun save(account: Account): Long
}