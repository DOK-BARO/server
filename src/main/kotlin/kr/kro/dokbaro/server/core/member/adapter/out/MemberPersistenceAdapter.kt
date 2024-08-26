package kr.kro.dokbaro.server.core.member.adapter.out

import kr.kro.dokbaro.server.common.annotation.PersistenceAdapter
import kr.kro.dokbaro.server.core.member.adapter.out.persistence.repository.jooq.MemberQueryRepository
import kr.kro.dokbaro.server.core.member.adapter.out.persistence.repository.jooq.MemberRepository
import kr.kro.dokbaro.server.core.member.application.port.out.LoadMemberByCertificationIdPort
import kr.kro.dokbaro.server.core.member.application.port.out.SaveMemberPort
import kr.kro.dokbaro.server.core.member.application.port.out.UpdateMemberPort
import kr.kro.dokbaro.server.core.member.domain.Member
import java.util.UUID

@PersistenceAdapter
class MemberPersistenceAdapter(
	private val memberRepository: MemberRepository,
	private val memberQueryRepository: MemberQueryRepository,
) : SaveMemberPort,
	LoadMemberByCertificationIdPort,
	UpdateMemberPort {
	override fun save(member: Member): Member = memberRepository.save(member)

	override fun update(member: Member) {
		memberRepository.update(member)
	}

	override fun findByCertificationId(certificationId: UUID): Member? =
		memberQueryRepository.findByCertificationId(certificationId)
}