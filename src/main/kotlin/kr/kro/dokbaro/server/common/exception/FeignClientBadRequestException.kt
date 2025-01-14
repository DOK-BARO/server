package kr.kro.dokbaro.server.common.exception

import kr.kro.dokbaro.server.common.exception.http.status4xx.BadRequestException

/**
 * Feign Client를 통한 요청 시 error를 처리합니다.
 *
 * @see FeignClientErrorDecoder
 */
class FeignClientBadRequestException(
	url: String? = null,
) : BadRequestException("bad request ${url ?: "external"}")