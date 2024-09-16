package kr.kro.dokbaro.server.core.member.adapter.out

import kr.kro.dokbaro.server.common.annotation.PersistenceAdapter
import kr.kro.dokbaro.server.core.member.adapter.out.persistence.repository.jooq.MemberQueryRepository
import kr.kro.dokbaro.server.core.member.adapter.out.persistence.repository.jooq.MemberRepository
import kr.kro.dokbaro.server.core.member.application.port.out.ExistMemberByEmailPort
import kr.kro.dokbaro.server.core.member.application.port.out.InsertMemberPort
import kr.kro.dokbaro.server.core.member.application.port.out.LoadMemberByCertificationIdPort
import kr.kro.dokbaro.server.core.member.application.port.out.UpdateMemberPort
import kr.kro.dokbaro.server.core.member.domain.Member
import java.util.UUID

@PersistenceAdapter
class MemberPersistenceAdapter(
	private val memberRepository: MemberRepository,
	private val memberQueryRepository: MemberQueryRepository,
) : InsertMemberPort,
	LoadMemberByCertificationIdPort,
	UpdateMemberPort,
	ExistMemberByEmailPort {
	override fun insert(member: Member): Member = memberRepository.insert(member)

	override fun update(member: Member) {
		memberRepository.update(member)
	}

	override fun findByCertificationId(certificationId: UUID): Member? =
		memberQueryRepository.findByCertificationId(certificationId)

	override fun existByEmail(email: String): Boolean = memberQueryRepository.existByEmail(email)
}