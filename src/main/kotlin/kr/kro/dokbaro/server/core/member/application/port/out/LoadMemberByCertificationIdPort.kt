package kr.kro.dokbaro.server.core.member.application.port.out

import kr.kro.dokbaro.server.core.member.domain.Member
import java.util.UUID

fun interface LoadMemberByCertificationIdPort {
	fun findByCertificationId(certificationId: UUID): Member?
}