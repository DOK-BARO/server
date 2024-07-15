package kr.kro.dokbaro.server.global.exception.http.status4xx

abstract class BadRequestException(
	message: String,
) : RuntimeException(message)