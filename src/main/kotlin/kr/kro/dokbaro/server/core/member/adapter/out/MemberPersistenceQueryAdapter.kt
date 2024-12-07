package kr.kro.dokbaro.server.core.member.adapter.out

import kr.kro.dokbaro.server.common.annotation.PersistenceAdapter
import kr.kro.dokbaro.server.core.member.adapter.out.persistence.repository.jooq.MemberQueryRepository
import kr.kro.dokbaro.server.core.member.application.port.out.ExistMemberByEmailPort
import kr.kro.dokbaro.server.core.member.application.port.out.LoadMemberPort
import kr.kro.dokbaro.server.core.member.application.port.out.dto.LoadMemberCondition
import kr.kro.dokbaro.server.core.member.domain.Member

@PersistenceAdapter
class MemberPersistenceQueryAdapter(
	private val memberQueryRepository: MemberQueryRepository,
) : ExistMemberByEmailPort
	override fun existByEmail(email: String): Boolean = memberQueryRepository.existByEmail(email)


}