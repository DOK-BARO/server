package kr.kro.dokbaro.server.domain.account.model.service

import kr.kro.dokbaro.server.domain.account.model.Account
import kr.kro.dokbaro.server.domain.account.port.input.query.FindOneAccountQuery
import kr.kro.dokbaro.server.domain.account.port.input.query.dto.AccountResponse
import kr.kro.dokbaro.server.domain.account.port.output.LoadAccountPort
import org.springframework.stereotype.Service

@Service
class AccountQueryService(
	private val loadAccountPort: LoadAccountPort,
) : FindOneAccountQuery {
	override fun getBy(socialId: String): AccountResponse {
		val account: Account = loadAccountPort.findBy(socialId) ?: throw AccountNotFoundException()

		return AccountResponse(account)
	}
}