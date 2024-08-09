package kr.kro.dokbaro.server.core.account.application.service

import kr.kro.dokbaro.server.core.account.application.port.input.query.FindAccountUseCase
import kr.kro.dokbaro.server.core.account.application.port.input.query.dto.AccountResult
import kr.kro.dokbaro.server.core.account.application.port.output.LoadAccountPort
import kr.kro.dokbaro.server.core.account.application.service.exception.NotFoundAccountException
import kr.kro.dokbaro.server.core.account.domain.Account
import org.springframework.stereotype.Service

@Service
class FindAccountQueryService(
	private val loadAccountPort: LoadAccountPort,
) : FindAccountUseCase {
	override fun getById(id: String): AccountResult {
		val account: Account = loadAccountPort.findBy(id) ?: throw NotFoundAccountException()

		return AccountResult(account)
	}
}