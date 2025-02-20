package kr.kro.dokbaro.server.core.member.application.port.input.query

import java.util.UUID

fun interface FindCertificationIdByEmailUseCase {
	fun findCertificationIdByEmail(email: String): UUID?
}