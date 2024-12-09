package kr.kro.dokbaro.server.core.member.application.port.out

import kr.kro.dokbaro.server.core.member.query.CertificatedMember
import java.util.UUID

fun interface ReadCertificatedMemberPort {
	fun findCertificatedMember(certificationId: UUID): CertificatedMember?
}