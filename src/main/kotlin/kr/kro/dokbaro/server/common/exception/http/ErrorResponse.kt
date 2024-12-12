package kr.kro.dokbaro.server.common.exception.http

/**
 * exception 상황에서 응답 상태 및 메세지를 담은 DTO입니다.
 */
data class ErrorResponse(
	val status: Int,
	val message: String?,
)