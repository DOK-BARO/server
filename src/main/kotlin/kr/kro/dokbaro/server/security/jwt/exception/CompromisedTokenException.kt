package kr.kro.dokbaro.server.security.jwt.exception

import org.springframework.security.core.AuthenticationException

class CompromisedTokenException : AuthenticationException("compromised refresh token")