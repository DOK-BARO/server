package kr.kro.dokbaro.server.common.exception.http.status4xx

abstract class UnauthorizedException(
	message: String = "Unauthorized",
) : RuntimeException(message)