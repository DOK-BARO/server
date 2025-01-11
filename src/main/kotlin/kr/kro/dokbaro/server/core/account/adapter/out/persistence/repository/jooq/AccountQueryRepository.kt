package kr.kro.dokbaro.server.core.account.adapter.out.persistence.repository.jooq

import kr.kro.dokbaro.server.core.account.domain.EmailAccount
import org.jooq.DSLContext
import org.jooq.generated.tables.JEmailAccount.EMAIL_ACCOUNT
import org.jooq.generated.tables.JMember.MEMBER
import org.springframework.stereotype.Repository

@Repository
class AccountQueryRepository(
	private val dslContext: DSLContext,
) {
	fun findByMemberId(memberId: Long): EmailAccount? =
		dslContext
			.select(
				EMAIL_ACCOUNT,
				EMAIL_ACCOUNT.ID,
				EMAIL_ACCOUNT.EMAIL,
				EMAIL_ACCOUNT.MEMBER_ID,
				EMAIL_ACCOUNT.PASSWORD,
			).from(EMAIL_ACCOUNT)
			.where(EMAIL_ACCOUNT.MEMBER_ID.eq(memberId))
			.fetchOneInto(EmailAccount::class.java)

	fun findByEmail(email: String): EmailAccount? =
		dslContext
			.select(
				EMAIL_ACCOUNT,
				EMAIL_ACCOUNT.ID,
				EMAIL_ACCOUNT.EMAIL,
				EMAIL_ACCOUNT.MEMBER_ID,
				EMAIL_ACCOUNT.PASSWORD,
			).from(EMAIL_ACCOUNT)
			.join(MEMBER)
			.on(MEMBER.ID.eq(EMAIL_ACCOUNT.MEMBER_ID))
			.where(MEMBER.EMAIL.eq(email))
			.fetchOneInto(EmailAccount::class.java)

	fun existsByEmail(email: String): Boolean =
		dslContext.fetchExists(EMAIL_ACCOUNT.where(EMAIL_ACCOUNT.EMAIL.eq(email).and(EMAIL_ACCOUNT.DELETED.isFalse)))
}