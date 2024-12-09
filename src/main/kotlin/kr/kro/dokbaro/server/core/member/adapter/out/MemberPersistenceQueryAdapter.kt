package kr.kro.dokbaro.server.core.member.adapter.out

import kr.kro.dokbaro.server.common.annotation.PersistenceAdapter
import kr.kro.dokbaro.server.core.member.adapter.out.persistence.repository.jooq.MemberQueryRepository
import kr.kro.dokbaro.server.core.member.application.port.out.ExistMemberByEmailPort
import kr.kro.dokbaro.server.core.member.application.port.out.LoadMemberByCertificationIdPort
import kr.kro.dokbaro.server.core.member.application.port.out.ReadCertificatedMemberPort
import kr.kro.dokbaro.server.core.member.application.port.out.ReadCertificationIdByEmailPort
import kr.kro.dokbaro.server.core.member.application.port.out.ReadEmailAuthenticationMemberPort
import kr.kro.dokbaro.server.core.member.domain.Member
import kr.kro.dokbaro.server.core.member.query.CertificatedMember
import kr.kro.dokbaro.server.core.member.query.EmailAuthenticationMember
import java.util.UUID

@PersistenceAdapter
class MemberPersistenceQueryAdapter(
	private val memberQueryRepository: MemberQueryRepository,
) : ReadEmailAuthenticationMemberPort,
	ReadCertificatedMemberPort,
	ExistMemberByEmailPort,
	LoadMemberByCertificationIdPort,
	ReadCertificationIdByEmailPort {
	override fun findEmailAuthenticationMember(email: String): EmailAuthenticationMember? =
		memberQueryRepository.findEmailAuthenticationMember(email)

	override fun findCertificatedMember(certificationId: UUID): CertificatedMember? =
		memberQueryRepository.findCertificatedMember(certificationId)

	override fun existByEmail(email: String): Boolean = memberQueryRepository.existByEmail(email)

	override fun findMemberByCertificationId(certificationId: UUID): Member? =
		memberQueryRepository.findMemberByCertificationId(certificationId)

	override fun findCertificationIdByEmail(email: String): UUID? = memberQueryRepository.findCertificationIdByEmail(email)
}