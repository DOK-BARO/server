package kr.kro.dokbaro.server.core.account.application.port.out

import kr.kro.dokbaro.server.core.account.domain.EmailAccount

/**
 * email 계정을 수정하는 port 입니다.
 */
fun interface UpdateEmailAccountPort {
	fun updateEmailAccount(emailAccount: EmailAccount)
}