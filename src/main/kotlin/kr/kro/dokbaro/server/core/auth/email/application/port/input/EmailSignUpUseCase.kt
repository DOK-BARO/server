package kr.kro.dokbaro.server.core.auth.email.application.port.input

import kr.kro.dokbaro.server.core.auth.email.application.port.input.dto.EmailSignUpCommand
import kr.kro.dokbaro.server.core.token.domain.AuthToken

fun interface EmailSignUpUseCase {
	fun signUp(command: EmailSignUpCommand): AuthToken
}