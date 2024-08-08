package kr.kro.dokbaro.server.core.auth.application.port.input

import kr.kro.dokbaro.server.core.auth.application.port.input.dto.ProviderAuthorizationCommand
import kr.kro.dokbaro.server.core.token.domain.AuthToken

fun interface OAuth2SignUpUseCase {
	fun signUp(command: ProviderAuthorizationCommand): AuthToken
}