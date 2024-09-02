package kr.kro.dokbaro.server.core.member.application.port.input.query

import kr.kro.dokbaro.server.core.member.application.port.input.dto.MemberResponse
import java.util.UUID

fun interface FindCertificatedMemberUseCase {
	fun getByCertificationId(certificationId: UUID): MemberResponse
}