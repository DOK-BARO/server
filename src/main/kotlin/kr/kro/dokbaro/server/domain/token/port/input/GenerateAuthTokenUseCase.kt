package kr.kro.dokbaro.server.domain.token.port.input

import kr.kro.dokbaro.server.domain.token.model.AuthToken

interface GenerateAuthTokenUseCase {
	fun generate(accountId: String): AuthToken
}