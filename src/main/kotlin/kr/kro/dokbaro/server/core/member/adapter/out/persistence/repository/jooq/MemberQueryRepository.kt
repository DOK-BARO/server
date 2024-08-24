package kr.kro.dokbaro.server.core.member.adapter.out.persistence.repository.jooq

import kr.kro.dokbaro.server.common.util.UUIDUtils
import kr.kro.dokbaro.server.core.member.adapter.out.persistence.entity.jooq.MemberMapper
import kr.kro.dokbaro.server.core.member.domain.Member
import org.jooq.DSLContext
import org.jooq.Record
import org.jooq.Result
import org.jooq.generated.tables.JMember
import org.jooq.generated.tables.JMemberEmail
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
		private val MEMBER_EMAIL = JMemberEmail.MEMBER_EMAIL
	}

	fun findByCertificationId(certificationId: UUID): Member? {
		val record: Map<MemberRecord, Result<Record>> =
			dslContext
				.select()
				.from(MEMBER)
				.join(MEMBER_ROLE)
				.on(MEMBER_ROLE.ID.eq(MEMBER.ID))
				.join(MEMBER_EMAIL)
				.on(MEMBER_EMAIL.ID.eq(MEMBER.ID))
				.where(MEMBER.CERTIFICATION_ID.eq(UUIDUtils.uuidToByteArray(certificationId)))
				.fetchGroups(MEMBER)

		return memberMapper.mapTo(record)
	}
}