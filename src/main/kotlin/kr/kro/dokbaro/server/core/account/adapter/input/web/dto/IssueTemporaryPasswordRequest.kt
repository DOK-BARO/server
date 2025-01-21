package kr.kro.dokbaro.server.core.account.adapter.input.web.dto

/**
 * 임시 비밀번호 발급시 요청값을 명시한 DTO 입니다.
 */
data class IssueTemporaryPasswordRequest(
	val email: String,
)