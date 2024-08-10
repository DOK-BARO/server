package kr.kro.dokbaro.server.core.account.application.port.input.command

import kr.kro.dokbaro.server.core.account.application.port.input.dto.AccountResult
import kr.kro.dokbaro.server.core.account.application.port.input.dto.RegisterAccountCommand

fun interface RegisterAccountUseCase {
	fun register(command: RegisterAccountCommand): AccountResult
}