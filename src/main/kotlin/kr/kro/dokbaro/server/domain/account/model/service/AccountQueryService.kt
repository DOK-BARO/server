package kr.kro.dokbaro.server.domain.account.model.service

import kr.kro.dokbaro.server.domain.account.model.Account
import kr.kro.dokbaro.server.domain.account.port.input.query.AccountFinder
import kr.kro.dokbaro.server.domain.account.port.input.query.dto.AccountResponse
import kr.kro.dokbaro.server.domain.account.port.output.LoadAccountPort
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