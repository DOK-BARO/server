package kr.kro.dokbaro.server.core.token.application.service.exception

import kr.kro.dokbaro.server.common.exception.http.status4xx.NotFoundException

class NotFoundTokenException : NotFoundException("token not found")