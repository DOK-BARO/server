package kr.kro.dokbaro.server.common.exception.http.status4xx

abstract class ForbiddenException(
	message: String = "Forbidden",
) : RuntimeException(message)