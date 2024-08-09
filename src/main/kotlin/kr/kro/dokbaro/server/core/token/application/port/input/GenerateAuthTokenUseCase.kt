package kr.kro.dokbaro.server.core.token.application.port.input

import kr.kro.dokbaro.server.core.token.domain.AuthToken
import kr.kro.dokbaro.server.core.token.domain.access.jwt.TokenClaims

fun interface GenerateAuthTokenUseCase {
	fun generate(claims: TokenClaims): AuthToken
}