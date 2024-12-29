package kr.kro.dokbaro.server.core.account.application.port.out

import kr.kro.dokbaro.server.core.account.domain.EmailAccount

interface LoadAccountPasswordPort {
	fun findByMemberId(memberId: Long): EmailAccount?

	fun findByEmail(email: String): EmailAccount?
}