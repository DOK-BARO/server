package kr.kro.dokbaro.server.core.account.adapter.out.persistence.repository.jooq

import kr.kro.dokbaro.server.core.account.domain.Account
import org.jooq.DSLContext
import org.jooq.generated.tables.JAccount
import org.springframework.stereotype.Repository

@Repository
class AccountRepository(
	private val dslContext: DSLContext,
) {
	companion object {
		private val ACCOUNT = JAccount.ACCOUNT
	}

	fun save(account: Account): Long {
		val accountId: Long =
			insertAccount(account)

		return accountId
	}

	private fun insertAccount(account: Account) =
		dslContext
			.insertInto(
				ACCOUNT,
				ACCOUNT.SOCIAL_ID,
				ACCOUNT.PROVIDER,
			).values(
				account.socialId,
				account.provider.name,
			).returningResult(ACCOUNT.ID)
			.fetchOneInto(Long::class.java)!!
}