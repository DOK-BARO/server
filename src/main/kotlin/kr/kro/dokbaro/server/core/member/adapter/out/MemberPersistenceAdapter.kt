package kr.kro.dokbaro.server.core.member.adapter.out

import kr.kro.dokbaro.server.common.annotation.PersistenceAdapter
import kr.kro.dokbaro.server.core.member.adapter.out.persistence.repository.jooq.MemberRepository
import kr.kro.dokbaro.server.core.member.application.port.out.InsertMemberPort
import kr.kro.dokbaro.server.core.member.application.port.out.UpdateMemberPort
import kr.kro.dokbaro.server.core.member.domain.Member

@PersistenceAdapter
class MemberPersistenceAdapter(
	private val memberRepository: MemberRepository,
) : InsertMemberPort,
	UpdateMemberPort {
	override fun insert(member: Member): Member = memberRepository.insert(member)

	override fun update(member: Member) {
		memberRepository.update(member)
	}
}