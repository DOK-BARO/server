package kr.kro.dokbaro.server.core.account.adapter.out.persistence.repository.jooq

import kr.kro.dokbaro.server.common.type.AuthProvider
import kr.kro.dokbaro.server.core.account.adapter.out.persistence.entity.jooq.AccountMapper
import kr.kro.dokbaro.server.core.account.domain.Account
import org.jooq.DSLContext
import org.jooq.Result
import org.jooq.generated.tables.JAccount
import org.jooq.generated.tables.records.AccountRecord
import org.jooq.generated.tables.records.AccountRoleRecord
import org.springframework.stereotype.Repository

@Repository
class AccountQueryRepository(
	private val dslContext: DSLContext,
	private val accountMapper: AccountMapper,
) {
	companion object {
		private val ACCOUNT = JAccount.ACCOUNT
	}

	fun findBy(socialId: String): Account? {
		val record: Map<AccountRecord, Result<AccountRoleRecord>> =
			dslContext
				.select()
				.from(ACCOUNT)
				.join(ACCOUNT_ROLE)
				.on(ACCOUNT_ROLE.ACCOUNT_ID.eq(ACCOUNT.ID))
				.where(ACCOUNT.SOCIAL_ID.eq(socialId))
				.fetchGroups(ACCOUNT, ACCOUNT_ROLE)

		return accountMapper.mapTo(record)
	}

	fun existBy(
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
}