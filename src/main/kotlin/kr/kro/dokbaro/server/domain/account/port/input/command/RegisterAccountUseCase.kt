package kr.kro.dokbaro.server.domain.account.port.input.command

import kr.kro.dokbaro.server.domain.account.port.input.command.dto.RegisterAccountCommand

interface RegisterAccountUseCase {
	fun registerIfNew(command: RegisterAccountCommand)
}