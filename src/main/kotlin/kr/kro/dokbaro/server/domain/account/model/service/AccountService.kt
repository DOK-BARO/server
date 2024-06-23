package kr.kro.dokbaro.server.domain.account.model.service

import kr.kro.dokbaro.server.domain.account.port.input.command.RegisterAccountUseCase
import kr.kro.dokbaro.server.domain.account.port.input.command.dto.RegisterAccountCommand
import org.springframework.stereotype.Service

@Service
class AccountService : RegisterAccountUseCase {
	override fun registerIfNew(command: RegisterAccountCommand) {
		TODO("Not yet implemented")
	}
}