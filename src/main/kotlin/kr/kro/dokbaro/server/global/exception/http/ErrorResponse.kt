package kr.kro.dokbaro.server.global.exception.http

data class ErrorResponse(
	val status: Int,
	val message: String?,
)