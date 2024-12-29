package kr.kro.dokbaro.server.core.member.application.port.input.query

import kr.kro.dokbaro.server.core.account.domain.AuthProvider
import java.util.UUID

fun interface FindCertificationIdBySocialUseCase {
	fun findCertificationIdBySocial(
		id: String,
		provider: AuthProvider,
	): UUID?
}