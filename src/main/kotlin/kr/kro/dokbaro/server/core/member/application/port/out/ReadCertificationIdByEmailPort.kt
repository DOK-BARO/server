package kr.kro.dokbaro.server.core.member.application.port.out

import java.util.UUID

fun interface ReadCertificationIdByEmailPort {
	fun findCertificationIdByEmail(email: String): UUID?
}