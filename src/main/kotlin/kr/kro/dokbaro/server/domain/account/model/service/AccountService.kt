package kr.kro.dokbaro.server.domain.account.model.service

import kr.kro.dokbaro.server.domain.account.model.Account
import kr.kro.dokbaro.server.domain.account.port.input.command.DisableAccountUseCase
import kr.kro.dokbaro.server.domain.account.port.input.command.RegisterAccountUseCase
import kr.kro.dokbaro.server.domain.account.port.input.command.dto.RegisterAccountCommand
import kr.kro.dokbaro.server.domain.account.port.input.query.dto.AccountResponse
import kr.kro.dokbaro.server.domain.account.port.output.ExistAccountPort
import kr.kro.dokbaro.server.domain.account.port.output.SaveAccountPort
import org.springframework.stereotype.Service
import java.time.Clock

@Service
class AccountService(
	private val existAccountPort: ExistAccountPort,
	private val saveAccountPort: SaveAccountPort,
	private val clock: Clock,
) : RegisterAccountUseCase,
	DisableAccountUseCase {
	override fun register(command: RegisterAccountCommand): AccountResponse {
		if (existAccountPort.existBy(command.socialId, command.provider)) {
			throw AlreadyExistAccountException(command.socialId, command.provider)
		}

		val account =
			Account.init(
				command.socialId,
				command.provider,
				clock,
			)

		val savedId: Long = saveAccountPort.save(account)

		return AccountResponse(
			savedId,
			account.roles.map { it.name },
			account.provider.name,
		)
	}
}