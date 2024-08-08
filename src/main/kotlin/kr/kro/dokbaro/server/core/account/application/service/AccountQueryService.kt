package kr.kro.dokbaro.server.core.account.application.service

import kr.kro.dokbaro.server.core.account.application.port.input.query.AccountFinder
import kr.kro.dokbaro.server.core.account.application.port.input.query.dto.AccountResponse
import kr.kro.dokbaro.server.core.account.application.port.output.LoadAccountPort
import kr.kro.dokbaro.server.core.account.domain.Account
import org.springframework.stereotype.Service

@Service
class AccountQueryService(
	private val loadAccountPort: LoadAccountPort,
) : AccountFinder {
	override fun getById(id: String): AccountResponse {
		val account: Account = loadAccountPort.findBy(id) ?: throw NotFoundAccountException()

		return AccountResponse(account)
	}
}