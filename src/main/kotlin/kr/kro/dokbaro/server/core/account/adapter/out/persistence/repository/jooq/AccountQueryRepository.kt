package kr.kro.dokbaro.server.core.account.adapter.out.persistence.repository.jooq

import kr.kro.dokbaro.server.core.account.domain.AccountPassword
import org.jooq.DSLContext
import org.jooq.generated.tables.JAccountPassword
import org.jooq.generated.tables.JMember
import org.springframework.stereotype.Repository

@Repository
class AccountQueryRepository(
	private val dslContext: DSLContext,
) {
	companion object {
		private val ACCOUNT_PASSWORD = JAccountPassword.ACCOUNT_PASSWORD
		private val MEMBER = JMember.MEMBER
	}

	fun findByMemberId(memberId: Long): AccountPassword? =
		dslContext
			.select(
				ACCOUNT_PASSWORD,
				ACCOUNT_PASSWORD.ID,
				ACCOUNT_PASSWORD.MEMBER_ID,
				ACCOUNT_PASSWORD.PASSWORD,
			).from(ACCOUNT_PASSWORD)
			.where(ACCOUNT_PASSWORD.MEMBER_ID.eq(memberId))
			.fetchOneInto(AccountPassword::class.java)

	fun findByEmail(email: String): AccountPassword? =
		dslContext
			.select(
				ACCOUNT_PASSWORD,
				ACCOUNT_PASSWORD.ID,
				ACCOUNT_PASSWORD.MEMBER_ID,
				ACCOUNT_PASSWORD.PASSWORD,
			).from(ACCOUNT_PASSWORD)
			.join(MEMBER)
			.on(MEMBER.ID.eq(ACCOUNT_PASSWORD.MEMBER_ID))
			.where(MEMBER.EMAIL.eq(email))
			.fetchOneInto(AccountPassword::class.java)
}