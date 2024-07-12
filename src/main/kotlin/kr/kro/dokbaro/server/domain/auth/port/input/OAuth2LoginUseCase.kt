package kr.kro.dokbaro.server.domain.auth.port.input

import kr.kro.dokbaro.server.domain.auth.port.input.dto.ProviderAuthorizationCommand

interface OAuth2LoginUseCase {
	fun login(command: ProviderAuthorizationCommand)
}