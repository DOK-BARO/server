package kr.kro.dokbaro.server.domain.auth.port.input

import kr.kro.dokbaro.server.domain.auth.port.input.dto.ProviderAuthorizationCommand

interface OAUth2SignUpUseCase {
	fun signUp(command: ProviderAuthorizationCommand)
}