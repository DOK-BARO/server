package kr.kro.dokbaro.server.core.termsofservice.adapter.out.persistence.repository.jooq

import kr.kro.dokbaro.server.core.termsofservice.application.port.out.dto.MemberAgreeTermsOfServiceElement
import org.jooq.DSLContext
import org.jooq.generated.tables.JAgreeTermsOfService
import org.springframework.stereotype.Repository

@Repository
class TermsOfServiceQueryRepository(
	private val dslContext: DSLContext,
) {
	companion object {
		private val AGREE_TERMS_OF_SERVICE = JAgreeTermsOfService.AGREE_TERMS_OF_SERVICE
	}

	fun findAllMemberAgree(memberId: Long): Collection<MemberAgreeTermsOfServiceElement> =
		dslContext
			.select(
				AGREE_TERMS_OF_SERVICE.TERMS_OF_SERVICE_ID,
			).from(AGREE_TERMS_OF_SERVICE)
			.where(AGREE_TERMS_OF_SERVICE.MEMBER_ID.eq(memberId))
			.fetchInto(Long::class.java)
			.map { MemberAgreeTermsOfServiceElement(it) }
}