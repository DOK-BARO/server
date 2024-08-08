package kr.kro.dokbaro.server.core.account.adapter.out.persistence.repository.jooq

import kr.kro.dokbaro.server.core.account.application.port.output.ExistAccountPort
import kr.kro.dokbaro.server.core.account.application.port.output.SaveAccountPort
import kr.kro.dokbaro.server.core.account.domain.Account
import kr.kro.dokbaro.server.core.account.domain.Role
import kr.kro.dokbaro.server.global.AuthProvider
import org.jooq.DSLContext
import org.jooq.generated.tables.JAccount
import org.jooq.generated.tables.JRole
import org.springframework.stereotype.Repository

@Repository
class AccountRepository(
	private val dslContext: DSLContext,
) : ExistAccountPort,
	SaveAccountPort {
	companion object {
		private val ACCOUNT = JAccount.ACCOUNT
		private val ROLE = JRole.ROLE
	}

	override fun existBy(
		socialId: String,
		provider: AuthProvider,
	): Boolean =
		dslContext
			.fetchExists(
				dslContext
					.selectOne()
					.from(ACCOUNT)
					.where(ACCOUNT.SOCIAL_ID.eq(socialId))
					.and(ACCOUNT.PROVIDER.eq(provider.name)),
			)

	override fun save(account: Account): Long {
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
				ACCOUNT.CREATED_AT,
			).values(
				account.socialId,
				account.provider.name,
				account.registeredAt,
			).returningResult(ACCOUNT.ID)
			.fetchOneInto(Long::class.java)!!

	private fun insertRoles(
		roles: Set<Role>,
		accountId: Long,
	) {
		roles.map { it.name }.forEach {
			dslContext
				.insertInto(
					ROLE,
					ROLE.ACCOUNT_ID,
					ROLE.NAME,
				).values(
					accountId,
					it,
				).execute()
		}
	}
}