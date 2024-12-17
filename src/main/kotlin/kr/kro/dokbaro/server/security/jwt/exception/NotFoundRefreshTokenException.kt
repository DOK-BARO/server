package kr.kro.dokbaro.server.security.jwt.exception

import org.springframework.security.core.AuthenticationException

class NotFoundRefreshTokenException : AuthenticationException("not found refresh token")