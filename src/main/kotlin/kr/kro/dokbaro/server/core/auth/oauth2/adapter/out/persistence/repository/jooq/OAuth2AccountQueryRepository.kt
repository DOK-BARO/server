package kr.kro.dokbaro.server.core.auth.oauth2.adapter.out.persistence.repository.jooq

import kr.kro.dokbaro.server.common.type.AuthProvider
import kr.kro.dokbaro.server.core.auth.oauth2.adapter.out.persistence.entity.jooq.OAuth2AccountMapper
import kr.kro.dokbaro.server.core.auth.oauth2.domain.OAuth2CertificatedAccount
import org.jooq.DSLContext
import org.jooq.Result
import org.jooq.generated.tables.JMember
import org.jooq.generated.tables.JMemberRole
import org.jooq.generated.tables.JOauth2Account
import org.jooq.generated.tables.records.MemberRecord
import org.jooq.generated.tables.records.MemberRoleRecord
import org.springframework.stereotype.Repository

@Repository
class OAuth2AccountQueryRepository(
	private val dslContext: DSLContext,
	private val oAuth2AccountMapper: OAuth2AccountMapper,
) {
	companion object {
		private val ACCOUNT = JOauth2Account.OAUTH2_ACCOUNT
		private val MEMBER = JMember.MEMBER
		private val MEMBER_ROLE = JMemberRole.MEMBER_ROLE
	}

	fun findBy(
		certificationId: String,
		provider: AuthProvider,
	): OAuth2CertificatedAccount? {
		val record: Map<MemberRecord, Result<MemberRoleRecord>> =
			dslContext
				.select()
				.from(
					ACCOUNT,
				).join(MEMBER)
				.on(
					MEMBER.ID.eq(
						ACCOUNT.ID,
					),
				).join(MEMBER_ROLE)
				.on(MEMBER_ROLE.MEMBER_ID.eq(MEMBER.ID))
				.where(
					ACCOUNT.SOCIAL_ID
						.eq(certificationId)
						.and(ACCOUNT.PROVIDER.eq(provider.name)),
				).fetchGroups(
					MEMBER,
					MEMBER_ROLE,
				)

		return oAuth2AccountMapper.mapTo(record)
	}

	fun existBy(
		socialId: String,
		provider: AuthProvider,
	): Boolean =
		dslContext
			.fetchExists(
				dslContext
					.selectOne()
					.from(
						ACCOUNT,
					).where(
						ACCOUNT.SOCIAL_ID
							.eq(
								socialId,
							),
					).and(
						ACCOUNT.PROVIDER
							.eq(
								provider.name,
							),
					),
			)
}