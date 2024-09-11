package kr.kro.dokbaro.server.core.auth.oauth2.adapter.out.persistence.repository.jooq

import kr.kro.dokbaro.server.core.auth.oauth2.domain.OAuth2Account
import org.jooq.DSLContext
import org.jooq.generated.tables.JOauth2Account
import org.springframework.stereotype.Repository

@Repository
class OAuth2AccountRepository(
	private val dslContext: DSLContext,
) {
	companion object {
		private val ACCOUNT = JOauth2Account.OAUTH2_ACCOUNT
	}

	fun insert(oAuth2Account: OAuth2Account): Long =
		dslContext
			.insertInto(
				ACCOUNT,
				ACCOUNT.SOCIAL_ID,
				ACCOUNT.PROVIDER,
				ACCOUNT.MEMBER_ID,
			).values(
				oAuth2Account.socialId,
				oAuth2Account.provider.name,
				oAuth2Account.memberId,
			).returningResult(
				ACCOUNT.ID,
			).fetchOneInto(Long::class.java)!!
}