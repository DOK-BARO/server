package kr.kro.dokbaro.server.core.termsofservice.adapter.out.persistence

import kr.kro.dokbaro.server.common.annotation.PersistenceAdapter
import kr.kro.dokbaro.server.core.termsofservice.adapter.out.persistence.repository.jooq.TermsOfServiceQueryRepository
import kr.kro.dokbaro.server.core.termsofservice.application.port.out.ReadMemberAgreeTermsOfServicePort
import kr.kro.dokbaro.server.core.termsofservice.application.port.out.dto.MemberAgreeTermsOfServiceElement

@PersistenceAdapter
class TermsOfServicePersistenceQueryAdapter(
	private val termsOfServiceQueryRepository: TermsOfServiceQueryRepository,
) : ReadMemberAgreeTermsOfServicePort {
	override fun findAll(memberId: Long): Collection<MemberAgreeTermsOfServiceElement> =
		termsOfServiceQueryRepository.findAllMemberAgree(memberId)
}