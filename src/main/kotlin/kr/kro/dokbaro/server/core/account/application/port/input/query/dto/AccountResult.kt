package kr.kro.dokbaro.server.core.account.application.port.input.query.dto

import kr.kro.dokbaro.server.core.account.domain.Account

data class AccountResult(
	val id: Long,
	val role: Collection<String>,
	val provider: String,
) {
	constructor(account: Account) :
		this(
			account.id,
			account.roles.map { it.name }.toSet(),
			account.provider.name,
		)
}