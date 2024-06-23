package kr.kro.dokbaro.server.domain.account.port.input.query.dto

data class AccountResponse(
	val id: String,
	val role: Set<String>,
	val provider: String,
)