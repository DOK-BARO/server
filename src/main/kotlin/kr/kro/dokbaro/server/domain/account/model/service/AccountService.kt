package kr.kro.dokbaro.server.domain.account.model.service

import kr.kro.dokbaro.server.domain.account.model.Account
import kr.kro.dokbaro.server.domain.account.model.Provider
import kr.kro.dokbaro.server.domain.account.port.input.command.RegisterAccountUseCase
import kr.kro.dokbaro.server.domain.account.port.input.command.dto.RegisterAccountCommand
import kr.kro.dokbaro.server.domain.account.port.output.ExistAccountPort
import kr.kro.dokbaro.server.domain.account.port.output.SaveAccountPort
import org.springframework.stereotype.Service
import java.time.Clock

@Service
class AccountService(
	private val existAccountPort: ExistAccountPort,
	private val saveAccountPort: SaveAccountPort,
	private val clock: Clock,
) : RegisterAccountUseCase {
	override fun registerIfNew(command: RegisterAccountCommand) {
		if (existAccountPort.notExistBy(command.socialId)) {
			saveAccountPort.save(
				Account.init(
					command.socialId,
					Provider.valueOf(command.provider),
					clock,
				),
			)
		}
	}
}