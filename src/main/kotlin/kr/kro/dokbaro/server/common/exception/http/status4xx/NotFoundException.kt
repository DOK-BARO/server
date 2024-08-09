package kr.kro.dokbaro.server.common.exception.http.status4xx

abstract class NotFoundException(
	message: String,
) : RuntimeException(message)