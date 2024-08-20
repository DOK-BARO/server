package kr.kro.dokbaro.server.core.account.adapter.out.persistence.repository.jooq

import kr.kro.dokbaro.server.core.account.domain.Account
import kr.kro.dokbaro.server.core.account.domain.Role
import org.jooq.DSLContext
import org.jooq.generated.tables.JAccount
import org.jooq.generated.tables.JAccountRole
import org.springframework.stereotype.Repository

@Repository
class AccountRepository(
	private val dslContext: DSLContext,
) {
	companion object {
		private val ACCOUNT = JAccount.ACCOUNT
		private val ACCOUNT_ROLE = JAccountRole.ACCOUNT_ROLE
	}

	fun save(account: Account): Long {
		val accountId: Long =
			insertAccount(account)
		insertRoles(account.roles, accountId)

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

	private fun insertRoles(
		roles: Set<Role>,
		accountId: Long,
	) {
		roles.map { it.name }.forEach {
			dslContext
				.insertInto(
					ACCOUNT_ROLE,
					ACCOUNT_ROLE.ACCOUNT_ID,
					ACCOUNT_ROLE.NAME,
				).values(
					accountId,
					it,
				).execute()
		}
	}
}