package kr.kro.dokbaro.server.security.jwt.exception

import org.springframework.security.core.AuthenticationException

class InvalidAccessTokenException : AuthenticationException("access token is invalid")