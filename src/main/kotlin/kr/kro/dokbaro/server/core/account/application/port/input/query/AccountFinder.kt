package kr.kro.dokbaro.server.core.account.application.port.input.query

import kr.kro.dokbaro.server.core.account.application.port.input.query.dto.AccountResponse

interface AccountFinder {
	fun getById(id: String): AccountResponse
}