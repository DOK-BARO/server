package kr.kro.dokbaro.server.core.termsofservice.adapter.out.persistence.repository.jooq

import kr.kro.dokbaro.server.core.termsofservice.domain.AgreeTermsOfService
import kr.kro.dokbaro.server.core.termsofservice.query.TermsOfServiceDetail
import org.jooq.DSLContext
import org.jooq.generated.tables.JAgreeTermsOfService.AGREE_TERMS_OF_SERVICE
import org.jooq.generated.tables.JTermsOfServiceDetail.TERMS_OF_SERVICE_DETAIL
import org.jooq.generated.tables.records.TermsOfServiceDetailRecord
import org.springframework.stereotype.Repository

@Repository
class TermsOfServiceRepository(
	private val dslContext: DSLContext,
) {
	fun getDetail(id: Long): TermsOfServiceDetail? {
		val record: TermsOfServiceDetailRecord? =
			dslContext
				.selectFrom(TERMS_OF_SERVICE_DETAIL)
				.where(TERMS_OF_SERVICE_DETAIL.TERMS_OF_SERVICE_ID.eq(id))
				.fetchOne()

		return record?.let { TermsOfServiceDetail(it.content) }
	}

	fun insertAgreeTermsOfService(agreeTermsOfService: AgreeTermsOfService) {
		val insertQuery =
			agreeTermsOfService.item.map {
				dslContext
					.insertInto(
						AGREE_TERMS_OF_SERVICE,
						AGREE_TERMS_OF_SERVICE.TERMS_OF_SERVICE_ID,
						AGREE_TERMS_OF_SERVICE.MEMBER_ID,
					).values(
						it.id,
						agreeTermsOfService.memberId,
					)
			}

		dslContext.batch(insertQuery).execute()
	}
}