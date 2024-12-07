package kr.kro.dokbaro.server.core.member.application.service

import kr.kro.dokbaro.server.core.member.application.port.input.query.FindCertificatedMemberUseCase
import kr.kro.dokbaro.server.core.member.application.port.input.query.FindEmailAuthenticationMemberUseCase
import kr.kro.dokbaro.server.core.member.query.CertificatedMember
import kr.kro.dokbaro.server.core.member.query.EmailAuthenticationMember
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class MemberQueryService :
	FindEmailAuthenticationMemberUseCase,
	FindCertificatedMemberUseCase {
	override fun findEmailAuthenticationMember(email: String): EmailAuthenticationMember {
		TODO("Not yet implemented")
	}

	override fun findCertificationMember(certificationId: UUID): CertificatedMember {
		TODO("Not yet implemented")
	}
}