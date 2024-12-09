package kr.kro.dokbaro.server.core.account.adapter.out.persistence.repository.jooq

import kr.kro.dokbaro.server.core.account.domain.AccountPassword
import kr.kro.dokbaro.server.core.account.domain.SocialAccount
import org.jooq.DSLContext
import org.jooq.generated.tables.JAccountPassword
import org.jooq.generated.tables.JOauth2Account
import org.springframework.stereotype.Repository

@Repository
class AccountRepository(
	private val dslContext: DSLContext,
) {
	companion object {
		private val OAUTH2_ACCOUNT = JOauth2Account.OAUTH2_ACCOUNT
		private val ACCOUNT_PASSWORD = JAccountPassword.ACCOUNT_PASSWORD
	}

	fun insertSocialAccount(socialAccount: SocialAccount) {
		dslContext
			.insertInto(
				OAUTH2_ACCOUNT,
				OAUTH2_ACCOUNT.SOCIAL_ID,
				OAUTH2_ACCOUNT.PROVIDER,
				OAUTH2_ACCOUNT.MEMBER_ID,
			).values(
				socialAccount.socialId,
				socialAccount.provider.name,
				socialAccount.memberId,
			).execute()
	}

	fun insertAccountPassword(accountPassword: AccountPassword) {
		dslContext
			.insertInto(
				ACCOUNT_PASSWORD,
				ACCOUNT_PASSWORD.PASSWORD,
				ACCOUNT_PASSWORD.MEMBER_ID,
			).values(
				accountPassword.password,
				accountPassword.memberId,
			).execute()
	}
}