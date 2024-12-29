package kr.kro.dokbaro.server.core.account.adapter.out.persistence.repository.jooq

import kr.kro.dokbaro.server.core.account.domain.EmailAccount
import kr.kro.dokbaro.server.core.account.domain.SocialAccount
import org.jooq.DSLContext
import org.jooq.generated.tables.JEmailAccount
import org.jooq.generated.tables.JOauth2Account
import org.springframework.stereotype.Repository

@Repository
class AccountRepository(
	private val dslContext: DSLContext,
) {
	companion object {
		private val OAUTH2_ACCOUNT = JOauth2Account.OAUTH2_ACCOUNT
		private val EMAIL_ACCOUNT = JEmailAccount.EMAIL_ACCOUNT
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

	fun insertEmailAccount(emailAccount: EmailAccount) {
		dslContext
			.insertInto(
				EMAIL_ACCOUNT,
				EMAIL_ACCOUNT.EMAIL,
				EMAIL_ACCOUNT.PASSWORD,
				EMAIL_ACCOUNT.MEMBER_ID,
			).values(
				emailAccount.email,
				emailAccount.password,
				emailAccount.memberId,
			).execute()
	}

	fun updateEmailAccount(emailAccount: EmailAccount) {
		dslContext
			.update(EMAIL_ACCOUNT)
			.set(EMAIL_ACCOUNT.EMAIL, emailAccount.email)
			.set(EMAIL_ACCOUNT.PASSWORD, emailAccount.password)
			.where(EMAIL_ACCOUNT.ID.eq(emailAccount.id))
			.execute()
	}
}