package kr.kro.dokbaro.server.common.dto.response

/**
 * id값만 응답 값으로 전송할 때 사용합니다
 */
data class IdResponse<T>(
	val id: T,
)