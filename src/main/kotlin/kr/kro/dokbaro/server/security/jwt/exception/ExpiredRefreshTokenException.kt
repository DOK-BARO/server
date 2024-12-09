package kr.kro.dokbaro.server.security.jwt.exception

import kr.kro.dokbaro.server.common.exception.http.status4xx.UnauthorizedException

class ExpiredRefreshTokenException : UnauthorizedException("expired refresh token")