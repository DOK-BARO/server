package kr.kro.dokbaro.server.core.member.application.service

import kr.kro.dokbaro.server.core.member.application.port.input.dto.CertificatedMember
import kr.kro.dokbaro.server.core.member.application.port.input.query.FindCertificatedMemberUseCase
import kr.kro.dokbaro.server.core.member.application.port.out.LoadMemberByCertificationIdPort
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class MemberQueryService(
	private val loadMemberByCertificationIdPort: LoadMemberByCertificationIdPort,
) : FindCertificatedMemberUseCase {
	override fun getByCertificationId(certificationId: UUID): CertificatedMember =
		CertificatedMember(
			member =
				loadMemberByCertificationIdPort.findByCertificationId(certificationId)
					?: throw NotFoundMemberException(),
		)
}