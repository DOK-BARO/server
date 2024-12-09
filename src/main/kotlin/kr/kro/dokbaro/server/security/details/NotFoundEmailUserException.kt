package kr.kro.dokbaro.server.security.details

import kr.kro.dokbaro.server.common.exception.http.status4xx.UnauthorizedException

class NotFoundEmailUserException : UnauthorizedException("User not found")