package kr.kro.dokbaro.server.core.member.application.port.out

import kr.kro.dokbaro.server.core.account.domain.AuthProvider
import java.util.UUID

fun interface ReadCertificationIdBySocialPort {
	fun findCertificationIdBySocial(
		id: String,
		provider: AuthProvider,
	): UUID?
}