package kr.kro.dokbaro.server.core.member.application.service

import kr.kro.dokbaro.server.core.member.application.port.input.query.FindCertificatedMemberUseCase
import kr.kro.dokbaro.server.core.member.application.port.out.LoadMemberByCertificationIdPort
import kr.kro.dokbaro.server.core.member.domain.Member
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class MemberQueryService(
	loadMemberByCertificationIdPort: LoadMemberByCertificationIdPort,
) : FindCertificatedMemberUseCase {
	override fun getByCertificationId(certificationId: UUID): Member {
		TODO("Not yet implemented")
	}
}