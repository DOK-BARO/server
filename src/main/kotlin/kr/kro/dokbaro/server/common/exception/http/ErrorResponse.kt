package kr.kro.dokbaro.server.common.exception.http

data class ErrorResponse(
	val status: Int,
	val message: String?,
)