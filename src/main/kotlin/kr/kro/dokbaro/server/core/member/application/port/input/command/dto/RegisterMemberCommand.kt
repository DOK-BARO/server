package kr.kro.dokbaro.server.core.member.application.port.input.command.dto

import kr.kro.dokbaro.server.core.member.domain.AccountType

data class RegisterMemberCommand(
	val nickname: String,
	val email: String?,
	val profileImage: String?,
	val accountType: AccountType,
)