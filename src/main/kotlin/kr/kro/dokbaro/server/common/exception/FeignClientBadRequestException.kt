package kr.kro.dokbaro.server.common.exception

import kr.kro.dokbaro.server.common.exception.http.status4xx.BadRequestException

class FeignClientBadRequestException(
	message: String?,
) : BadRequestException("bad request ${message ?: "external"}")