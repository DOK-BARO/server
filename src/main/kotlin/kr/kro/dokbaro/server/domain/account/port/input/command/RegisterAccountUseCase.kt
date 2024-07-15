package kr.kro.dokbaro.server.domain.account.port.input.command

import kr.kro.dokbaro.server.domain.account.port.input.command.dto.RegisterAccountCommand
import kr.kro.dokbaro.server.domain.account.port.input.query.dto.AccountResponse

fun interface RegisterAccountUseCase {
	fun register(command: RegisterAccountCommand): AccountResponse
}