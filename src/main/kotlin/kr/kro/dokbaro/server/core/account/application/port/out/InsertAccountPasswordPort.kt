package kr.kro.dokbaro.server.core.account.application.port.out

import kr.kro.dokbaro.server.core.account.domain.EmailAccount

/**
 * 계정 비밀번호를 insert 하는 port 입니다.
 */
fun interface InsertAccountPasswordPort {
	fun insertEmailAccount(emailAccount: EmailAccount)
}