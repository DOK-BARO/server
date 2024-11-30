package kr.kro.dokbaro.server.common.exception

import kr.kro.dokbaro.server.common.exception.http.status4xx.BadRequestException

class FeignClientBadRequestException(
	url: String? = null,
) : BadRequestException("bad request ${url ?: "external"}")