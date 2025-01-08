package kr.kro.dokbaro.server.core.member.adapter.out.persistence.repository.jooq

import kr.kro.dokbaro.server.common.util.UUIDUtils
import kr.kro.dokbaro.server.core.account.domain.AuthProvider
import kr.kro.dokbaro.server.core.member.adapter.out.persistence.entity.jooq.MemberMapper
import kr.kro.dokbaro.server.core.member.domain.Member
import kr.kro.dokbaro.server.core.member.query.CertificatedMember
import kr.kro.dokbaro.server.core.member.query.EmailAuthenticationMember
import org.jooq.DSLContext
import org.jooq.Record
import org.jooq.Result
import org.jooq.generated.tables.JEmailAccount
import org.jooq.generated.tables.JMember
import org.jooq.generated.tables.JMemberRole
import org.jooq.generated.tables.JOauth2Account
import org.jooq.generated.tables.records.MemberRecord
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class MemberQueryRepository(
	private val dslContext: DSLContext,
	private val memberMapper: MemberMapper,
) {
	companion object {
		private val MEMBER = JMember.MEMBER
		private val MEMBER_ROLE = JMemberRole.MEMBER_ROLE
		private val EMAIL_ACCOUNT = JEmailAccount.EMAIL_ACCOUNT
		private val OAUTH2_ACCOUNT = JOauth2Account.OAUTH2_ACCOUNT
	}

	fun findEmailAuthenticationMember(email: String): EmailAuthenticationMember? {
		val record: Result<out Record> =
			dslContext
				.select(
					MEMBER.ID,
					MEMBER.CERTIFICATION_ID,
					MEMBER.NICKNAME,
					MEMBER.EMAIL,
					MEMBER_ROLE.NAME,
					EMAIL_ACCOUNT.PASSWORD,
				).from(MEMBER)
				.join(MEMBER_ROLE)
				.on(MEMBER_ROLE.MEMBER_ID.eq(MEMBER.ID))
				.leftJoin(EMAIL_ACCOUNT)
				.on(EMAIL_ACCOUNT.MEMBER_ID.eq(MEMBER.ID))
				.where(MEMBER.EMAIL.eq(email).and(MEMBER.WITHDRAW.isFalse.and(MEMBER.DELETED.isFalse)))
				.fetch()

		return memberMapper.toEmailAuthenticationMember(record)
	}

	fun findCertificatedMember(certificationId: UUID): CertificatedMember? {
		val record: Result<out Record> =
			dslContext
				.select(
					MEMBER.ID,
					MEMBER.CERTIFICATION_ID,
					MEMBER.NICKNAME,
					MEMBER.EMAIL,
					MEMBER_ROLE.NAME,
				).from(MEMBER)
				.join(MEMBER_ROLE)
				.on(MEMBER_ROLE.MEMBER_ID.eq(MEMBER.ID))
				.where(
					MEMBER.CERTIFICATION_ID
						.eq(
							UUIDUtils.uuidToByteArray(certificationId),
						).and(MEMBER.WITHDRAW.isFalse.and(MEMBER.DELETED.isFalse)),
				).fetch()

		return memberMapper.toCertificatedMember(record)
	}

	fun findMemberByCertificationId(certificationId: UUID): Member? {
		val record: Map<MemberRecord, Result<Record>> =
			dslContext
				.select()
				.from(MEMBER)
				.join(MEMBER_ROLE)
				.on(MEMBER_ROLE.MEMBER_ID.eq(MEMBER.ID))
				.where(
					MEMBER.CERTIFICATION_ID
						.eq(
							UUIDUtils.uuidToByteArray(certificationId),
						).and(MEMBER.WITHDRAW.isFalse.and(MEMBER.DELETED.isFalse)),
				).fetchGroups(MEMBER)

		return memberMapper.toMember(record)
	}

	fun findCertificationIdByEmail(email: String): UUID? =
		dslContext
			.select(MEMBER.CERTIFICATION_ID)
			.from(MEMBER)
			.where(MEMBER.EMAIL.eq(email).and(MEMBER.WITHDRAW.isFalse.and(MEMBER.DELETED.isFalse)))
			.fetchOne {
				UUIDUtils.byteArrayToUUID(it.value1())
			}

	fun findCertificationIdBySocial(
		id: String,
		provider: AuthProvider,
	): UUID? =
		dslContext
			.select(MEMBER.CERTIFICATION_ID)
			.from(MEMBER)
			.join(OAUTH2_ACCOUNT)
			.on(OAUTH2_ACCOUNT.MEMBER_ID.eq(MEMBER.ID))
			.where(
				OAUTH2_ACCOUNT.SOCIAL_ID
					.eq(
						id,
					).and(OAUTH2_ACCOUNT.PROVIDER.eq(provider.name))
					.and(MEMBER.WITHDRAW.isFalse.and(MEMBER.DELETED.isFalse)),
			).fetchOne {
				UUIDUtils.byteArrayToUUID(it.value1())
			}
}