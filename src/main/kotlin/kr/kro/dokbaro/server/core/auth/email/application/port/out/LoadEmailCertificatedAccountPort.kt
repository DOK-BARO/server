package kr.kro.dokbaro.server.core.auth.email.application.port.out

import kr.kro.dokbaro.server.core.auth.email.domain.EmailCertificatedAccount

fun interface LoadEmailCertificatedAccountPort {
	fun findByEmail(email: String): EmailCertificatedAccount?
}