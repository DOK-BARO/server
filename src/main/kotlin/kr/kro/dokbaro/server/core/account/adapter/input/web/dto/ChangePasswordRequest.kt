package kr.kro.dokbaro.server.core.account.adapter.input.web.dto

/**
 * 비밀번호 변경 시 요청값을 명시한 DTO 입니다.
 */
data class ChangePasswordRequest(
	val oldPassword: String,
	val newPassword: String,
)