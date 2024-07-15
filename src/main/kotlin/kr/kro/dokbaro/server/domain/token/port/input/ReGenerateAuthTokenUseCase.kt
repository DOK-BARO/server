package kr.kro.dokbaro.server.domain.token.port.input

import kr.kro.dokbaro.server.domain.token.model.AuthToken

fun interface ReGenerateAuthTokenUseCase {
	fun reGenerate(refreshToken: String): AuthToken
}