package kr.kro.dokbaro.server.domain.account.port.output

import kr.kro.dokbaro.server.domain.account.model.Account

fun interface SaveAccountPort {
	fun save(account: Account): Long
}