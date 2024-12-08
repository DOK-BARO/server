package kr.kro.dokbaro.server.security.jwt.refresh

import kr.kro.dokbaro.server.common.exception.http.status4xx.UnauthorizedException

class AlreadyUsedRefreshTokenException : UnauthorizedException("refresh token already used")