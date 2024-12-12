package kr.kro.dokbaro.server.core.account.application.port.out

import kr.kro.dokbaro.server.core.account.domain.AccountPassword

interface LoadAccountPasswordPort {
	fun findByMemberId(memberId: Long): AccountPassword?

	fun findByEmail(email: String): AccountPassword?
}