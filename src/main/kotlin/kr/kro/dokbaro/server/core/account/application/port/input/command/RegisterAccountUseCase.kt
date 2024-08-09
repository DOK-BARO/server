package kr.kro.dokbaro.server.core.account.application.port.input.command

import kr.kro.dokbaro.server.core.account.application.port.input.command.dto.RegisterAccountCommand
import kr.kro.dokbaro.server.core.account.application.port.input.query.dto.AccountResult

fun interface RegisterAccountUseCase {
	fun register(command: RegisterAccountCommand): AccountResult
}