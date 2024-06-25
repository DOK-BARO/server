package kr.kro.dokbaro.server.domain.account.port.input.query.dto

import kr.kro.dokbaro.server.domain.account.model.Account

data class AccountResponse(
	val id: Long,
	val role: Set<String>,
	val provider: String,
) {
	constructor(account: Account) :
		this(
			account.id,
			account.roles.map { it.name }.toSet(),
			account.provider.name,
		)
}