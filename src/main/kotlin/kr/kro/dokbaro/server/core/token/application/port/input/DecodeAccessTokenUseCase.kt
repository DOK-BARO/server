package kr.kro.dokbaro.server.core.token.application.port.input

import kr.kro.dokbaro.server.core.token.domain.access.jwt.TokenClaims

fun interface DecodeAccessTokenUseCase {
	fun decode(accessToken: String): TokenClaims
}