package kr.kro.dokbaro.server.core.auth.email.adapter.out.persistence.repository.jooq

import kr.kro.dokbaro.server.core.auth.email.domain.AccountPassword
import org.jooq.DSLContext
import org.jooq.generated.tables.JAccountPassword
import org.springframework.stereotype.Repository

@Repository
class AccountPasswordRepository(
	private val dslContext: DSLContext,
) {
	companion object {
		private val ACCOUNT_PASSWORD = JAccountPassword.ACCOUNT_PASSWORD
	}

	fun insert(accountPassword: AccountPassword): Long =
		dslContext
			.insertInto(
				ACCOUNT_PASSWORD,
				ACCOUNT_PASSWORD.PASSWORD,
				ACCOUNT_PASSWORD.MEMBER_ID,
			).values(
				accountPassword.password,
				accountPassword.memberId,
			).returningResult(ACCOUNT_PASSWORD.ID)
			.fetchOneInto(Long::class.java)!!
}