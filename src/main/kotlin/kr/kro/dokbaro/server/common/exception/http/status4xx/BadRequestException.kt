package kr.kro.dokbaro.server.common.exception.http.status4xx

abstract class BadRequestException(
	message: String,
) : RuntimeException(message)