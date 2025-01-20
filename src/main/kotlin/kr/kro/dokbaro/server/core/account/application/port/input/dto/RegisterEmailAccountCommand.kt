package kr.kro.dokbaro.server.core.account.application.port.input.dto

/**
 * email 계정 등록에 필요한 Command DTO 입니다.
 */
data class RegisterEmailAccountCommand(
	val email: String,
	val nickname: String,
	val password: String,
	val profileImage: String?,
)