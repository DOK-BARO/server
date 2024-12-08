package kr.kro.dokbaro.server.core.member.adapter.out.persistence.repository.jooq

import kr.kro.dokbaro.server.common.util.UUIDUtils
import kr.kro.dokbaro.server.core.member.adapter.out.persistence.entity.jooq.MemberMapper
import kr.kro.dokbaro.server.core.member.domain.Member
import kr.kro.dokbaro.server.core.member.query.CertificatedMember
import kr.kro.dokbaro.server.core.member.query.EmailAuthenticationMember
import org.jooq.DSLContext
import org.jooq.Record
import org.jooq.Result
import org.jooq.generated.tables.JAccountPassword
import org.jooq.generated.tables.JMember
import org.jooq.generated.tables.JMemberRole
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
		private val ACCOUNT_PASSWORD = JAccountPassword.ACCOUNT_PASSWORD
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
					ACCOUNT_PASSWORD.PASSWORD,
				).from(MEMBER)
				.join(MEMBER_ROLE)
				.on(MEMBER_ROLE.MEMBER_ID.eq(MEMBER.ID))
				.leftJoin(ACCOUNT_PASSWORD)
				.on(ACCOUNT_PASSWORD.MEMBER_ID.eq(MEMBER.ID))
				.where(MEMBER.EMAIL.eq(email))
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
				.where(MEMBER.CERTIFICATION_ID.eq(UUIDUtils.uuidToByteArray(certificationId)))
				.fetch()

		return memberMapper.toCertificatedMember(record)
	}

	fun existByEmail(email: String): Boolean = dslContext.fetchExists(MEMBER.where(MEMBER.EMAIL.eq(email)))

	fun findMemberByCertificationId(certificationId: UUID): Member? {
		val record: Map<MemberRecord, Result<Record>> =
			dslContext
				.select()
				.from(MEMBER)
				.join(MEMBER_ROLE)
				.on(MEMBER_ROLE.MEMBER_ID.eq(MEMBER.ID))
				.where(MEMBER.CERTIFICATION_ID.eq(UUIDUtils.uuidToByteArray(certificationId)))
				.fetchGroups(MEMBER)

		return memberMapper.toMember(record)
	}

	fun findCertificationIdByEmail(email: String): UUID? {
		val record =
			dslContext
				.select(MEMBER.CERTIFICATION_ID)
				.from(MEMBER)
				.where(MEMBER.EMAIL.eq(email))
				.fetchOneInto(ByteArray::class.java)

		return record?.let { UUIDUtils.byteArrayToUUID(it) }
	}
}