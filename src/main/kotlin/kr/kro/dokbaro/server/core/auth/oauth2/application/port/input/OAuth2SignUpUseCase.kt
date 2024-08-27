package kr.kro.dokbaro.server.core.auth.oauth2.application.port.input

import kr.kro.dokbaro.server.core.auth.oauth2.application.port.dto.LoadProviderAccountCommand
import kr.kro.dokbaro.server.core.token.domain.AuthToken

fun interface OAuth2SignUpUseCase {
	fun signUp(command: LoadProviderAccountCommand): AuthToken
}