package kr.kro.dokbaro.server.core.token.application.port.input

import kr.kro.dokbaro.server.core.token.domain.AuthToken

fun interface ReGenerateAuthTokenUseCase {
	fun reGenerate(refreshToken: String): AuthToken
}