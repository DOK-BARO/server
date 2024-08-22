package kr.kro.dokbaro.server.core.account.application.service

import kr.kro.dokbaro.server.core.account.application.port.input.dto.AccountResult
import kr.kro.dokbaro.server.core.account.application.port.input.query.FindCertificationAccountUseCase
import kr.kro.dokbaro.server.core.account.application.port.out.LoadAccountPort
import kr.kro.dokbaro.server.core.account.application.service.exception.NotFoundAccountException
import kr.kro.dokbaro.server.core.account.domain.Account
import org.springframework.stereotype.Service

@Service
class AccountQueryService(
	private val loadAccountPort: LoadAccountPort,
) : FindCertificationAccountUseCase {
	override fun getById(id: String): AccountResult {
		val account: Account = loadAccountPort.findBy(id) ?: throw NotFoundAccountException()

		return AccountResult(account)
	}
}