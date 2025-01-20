package kr.kro.dokbaro.server.core.account.application.port.out

import kr.kro.dokbaro.server.core.account.domain.EmailAccount

/**
 * email 계정을 로딩하는 port 입니다.
 */
interface LoadEmailAccountPort {
	fun findByMemberId(memberId: Long): EmailAccount?

	fun findByEmail(email: String): EmailAccount?
}