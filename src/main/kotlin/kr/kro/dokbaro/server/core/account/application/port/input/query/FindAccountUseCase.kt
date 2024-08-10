package kr.kro.dokbaro.server.core.account.application.port.input.query

import kr.kro.dokbaro.server.core.account.application.port.input.dto.AccountResult

interface FindAccountUseCase {
	fun getById(id: String): AccountResult
}