package kr.kro.dokbaro.server.domain.account.adapter.output.jooq

import kr.kro.dokbaro.server.domain.account.model.Account
import kr.kro.dokbaro.server.domain.account.port.output.LoadAccountPort
import org.jooq.DSLContext
import org.jooq.generated.tables.JAccount
import org.springframework.stereotype.Repository

@Repository
class AccountJooqQueryRepository(
	private val dslContext: DSLContext,
) : LoadAccountPort {
	private val account: JAccount = JAccount.ACCOUNT

	override fun findBy(socialId: String): Account? =
		dslContext
			.select(
				account.ID,
				account.SOCIAL_ID,
				account.PROVIDER,
				account.CREATED_AT,
			).from(account)
			.where(account.SOCIAL_ID.eq(socialId))
			.fetchOneInto(Account::class.java)
}