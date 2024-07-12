package kr.kro.dokbaro.server.domain.token.port.input

import kr.kro.dokbaro.server.domain.token.model.access.jwt.TokenClaims

interface DecodeAccessTokenUseCase {
	fun decode(accessToken: String): TokenClaims
}