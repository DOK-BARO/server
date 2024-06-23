package kr.kro.dokbaro.server.domain.account.model.service

import kr.kro.dokbaro.server.domain.account.port.input.query.FindOneAccountQuery
import kr.kro.dokbaro.server.domain.account.port.input.query.dto.AccountResponse
import org.springframework.stereotype.Service

@Service
class AccountQueryService : FindOneAccountQuery {
	override fun findBy(socialId: String): AccountResponse {
		TODO("Not yet implemented")
	}
}