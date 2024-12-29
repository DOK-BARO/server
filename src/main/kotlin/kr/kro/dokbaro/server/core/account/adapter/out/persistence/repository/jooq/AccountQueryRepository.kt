package kr.kro.dokbaro.server.core.account.adapter.out.persistence.repository.jooq

import kr.kro.dokbaro.server.core.account.domain.EmailAccount
import org.jooq.DSLContext
import org.jooq.generated.tables.JEmailAccount
import org.jooq.generated.tables.JMember
import org.springframework.stereotype.Repository

@Repository
class AccountQueryRepository(
	private val dslContext: DSLContext,
) {
	companion object {
		private val EMAIL_ACCOUNT = JEmailAccount.EMAIL_ACCOUNT
		private val MEMBER = JMember.MEMBER
	}

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
}