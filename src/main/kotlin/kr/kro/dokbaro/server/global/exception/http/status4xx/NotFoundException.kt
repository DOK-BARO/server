package kr.kro.dokbaro.server.global.exception.http.status4xx

abstract class NotFoundException(
	message: String,
) : RuntimeException(message)