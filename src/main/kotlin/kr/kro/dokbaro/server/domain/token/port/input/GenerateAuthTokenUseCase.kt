package kr.kro.dokbaro.server.domain.token.port.input

import kr.kro.dokbaro.server.domain.token.model.AuthToken
import kr.kro.dokbaro.server.domain.token.model.access.jwt.TokenClaims

fun interface GenerateAuthTokenUseCase {
	fun generate(claims: TokenClaims): AuthToken
}