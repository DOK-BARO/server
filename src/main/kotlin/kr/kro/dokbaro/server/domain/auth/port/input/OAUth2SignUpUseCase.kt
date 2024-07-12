package kr.kro.dokbaro.server.domain.auth.port.input

import kr.kro.dokbaro.server.domain.auth.port.input.dto.ProviderAuthorizationCommand
import kr.kro.dokbaro.server.domain.token.model.AuthToken

interface OAUth2SignUpUseCase {
	fun signUp(command: ProviderAuthorizationCommand): AuthToken
}