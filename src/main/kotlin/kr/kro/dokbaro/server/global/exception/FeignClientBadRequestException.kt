package kr.kro.dokbaro.server.global.exception

import kr.kro.dokbaro.server.global.exception.http.status4xx.BadRequestException

class FeignClientBadRequestException(
	message: String?,
) : BadRequestException("bad request ${message ?: "external"}")