package kr.kro.dokbaro.server.core.member.adapter.out

import kr.kro.dokbaro.server.common.annotation.PersistenceAdapter
import kr.kro.dokbaro.server.core.account.domain.AuthProvider
import kr.kro.dokbaro.server.core.member.adapter.out.persistence.repository.jooq.MemberQueryRepository
import kr.kro.dokbaro.server.core.member.application.port.out.LoadMemberByCertificationIdPort
import kr.kro.dokbaro.server.core.member.application.port.out.ReadCertificatedMemberPort
import kr.kro.dokbaro.server.core.member.application.port.out.ReadCertificationIdByEmailPort
import kr.kro.dokbaro.server.core.member.application.port.out.ReadCertificationIdBySocialPort
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
	LoadMemberByCertificationIdPort,
	ReadCertificationIdByEmailPort,
	ReadCertificationIdBySocialPort {
	override fun findEmailAuthenticationMember(email: String): EmailAuthenticationMember? =
		memberQueryRepository.findEmailAuthenticationMember(email)

	override fun findCertificatedMember(certificationId: UUID): CertificatedMember? =
		memberQueryRepository.findCertificatedMember(certificationId)

	override fun findMemberByCertificationId(certificationId: UUID): Member? =
		memberQueryRepository.findMemberByCertificationId(certificationId)

	override fun findCertificationIdByEmail(email: String): UUID? = memberQueryRepository.findCertificationIdByEmail(email)

	override fun findCertificationIdBySocial(
		id: String,
		provider: AuthProvider,
	): UUID? = memberQueryRepository.findCertificationIdBySocial(id, provider)
}