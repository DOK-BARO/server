package kr.kro.dokbaro.server.core.token.application.service.exception

import kr.kro.dokbaro.server.common.exception.http.status4xx.ForbiddenException

class CompromisedTokenException : ForbiddenException("compromised token")