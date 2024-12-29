package kr.kro.dokbaro.server.core.account.application.port.out

import kr.kro.dokbaro.server.core.account.domain.EmailAccount

fun interface UpdateEmailAccountPort {
	fun updateEmailAccount(emailAccount: EmailAccount)
}