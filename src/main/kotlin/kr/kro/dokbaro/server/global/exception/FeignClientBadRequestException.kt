package kr.kro.dokbaro.server.global.exception

import kr.kro.dokbaro.server.global.exception.http.status4xx.BadRequestException

class FeignClientBadRequestException : BadRequestException("feign client bad request")