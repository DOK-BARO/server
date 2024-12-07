package kr.kro.dokbaro.server.core.member.application.port.input.query

import kr.kro.dokbaro.server.core.member.query.CertificatedMember
import java.util.UUID

fun interface FindCertificatedMemberUseCase {
	fun findCertificationMember(certificationId: UUID): CertificatedMember
}