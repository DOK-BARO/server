package kr.kro.dokbaro.server.domain.account.port.input.query

import kr.kro.dokbaro.server.domain.account.port.input.query.dto.AccountResponse

interface FindOneAccountQuery {
	fun findBy(socialId: String): AccountResponse
}