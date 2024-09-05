package kr.kro.dokbaro.server.core.auth.email.application.port.input

import kr.kro.dokbaro.server.core.auth.email.application.port.input.dto.EmailLoginCommand
import kr.kro.dokbaro.server.core.token.domain.AuthToken

fun interface EmailLoginUseCase {
	fun login(command: EmailLoginCommand): AuthToken
}