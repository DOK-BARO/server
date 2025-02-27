package kr.kro.dokbaro.server.core.emailauthentication.adapter.out.persistence.repository.jooq

import kr.kro.dokbaro.server.core.emailauthentication.adapter.out.persistence.entity.jooq.EmailAuthenticationMapper
import kr.kro.dokbaro.server.core.emailauthentication.application.port.out.dto.SearchEmailAuthenticationCondition
import kr.kro.dokbaro.server.core.emailauthentication.domain.EmailAuthentication
import org.jooq.Condition
import org.jooq.DSLContext
import org.jooq.generated.tables.JEmailAuthentication.EMAIL_AUTHENTICATION
import org.jooq.generated.tables.records.EmailAuthenticationRecord
import org.jooq.impl.DSL
import org.springframework.stereotype.Repository

@Repository
class EmailAuthenticationQueryRepository(
	private val dslContext: DSLContext,
	private val emailAuthenticationMapper: EmailAuthenticationMapper,
) {
	fun existBy(condition: SearchEmailAuthenticationCondition): Boolean =
		dslContext.fetchExists(EMAIL_AUTHENTICATION.where(buildCondition(condition)))

	fun findBy(condition: SearchEmailAuthenticationCondition): EmailAuthentication? {
		val record: EmailAuthenticationRecord? =
			dslContext
				.selectFrom(EMAIL_AUTHENTICATION)
				.where(buildCondition(condition))
				.fetchOne()

		return emailAuthenticationMapper.toEmailAuthentication(record)
	}

	private fun buildCondition(condition: SearchEmailAuthenticationCondition): Condition =
		DSL.and(
			condition.address?.let {
				EMAIL_AUTHENTICATION.ADDRESS.eq(condition.address)
			},
			condition.code?.let {
				EMAIL_AUTHENTICATION.CODE.eq(condition.code)
			},
			condition.authenticated?.let {
				EMAIL_AUTHENTICATION.AUTHENTICATED.eq(condition.authenticated)
			},
			condition.used?.let {
				EMAIL_AUTHENTICATION.USED.eq(condition.used)
			},
		)
}