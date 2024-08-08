package kr.kro.dokbaro.server.core.account.adapter.out.persistence.repository.jooq

import kr.kro.dokbaro.server.core.account.adapter.out.persistence.entity.jooq.AccountMapper
import kr.kro.dokbaro.server.core.account.application.port.output.LoadAccountPort
import kr.kro.dokbaro.server.core.account.domain.Account
import org.jooq.DSLContext
import org.jooq.Result
import org.jooq.generated.tables.JAccount
import org.jooq.generated.tables.JRole
import org.jooq.generated.tables.records.AccountRecord
import org.jooq.generated.tables.records.RoleRecord
import org.springframework.stereotype.Repository

@Repository
class AccountQueryRepository(
	private val dslContext: DSLContext,
	private val accountMapper: AccountMapper,
) : LoadAccountPort {
	companion object {
		private val ACCOUNT = JAccount.ACCOUNT
		private val ROLE = JRole.ROLE
	}

	override fun findBy(socialId: String): Account? {
		val record: Map<AccountRecord, Result<RoleRecord>> =
			dslContext
				.select()
				.from(ACCOUNT)
				.join(ROLE)
				.on(ROLE.ACCOUNT_ID.eq(ACCOUNT.ID))
				.where(ACCOUNT.SOCIAL_ID.eq(socialId))
				.fetchGroups(ACCOUNT, ROLE)

		return accountMapper.mapTo(record)
	}
}