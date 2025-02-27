package kr.kro.dokbaro.server.core.member.adapter.out.persistence.repository.jooq

import kr.kro.dokbaro.server.common.util.UUIDUtils
import kr.kro.dokbaro.server.core.member.adapter.out.persistence.entity.jooq.MemberMapper
import kr.kro.dokbaro.server.core.member.domain.Member
import kr.kro.dokbaro.server.core.member.domain.Role
import org.jooq.DSLContext
import org.jooq.Record
import org.jooq.Result
import org.jooq.generated.tables.JMember.MEMBER
import org.jooq.generated.tables.JMemberRole.MEMBER_ROLE
import org.jooq.generated.tables.records.MemberRecord
import org.springframework.stereotype.Repository

@Repository
class MemberRepository(
	private val dslContext: DSLContext,
	private val memberMapper: MemberMapper,
) {
	fun insert(member: Member): Member {
		val memberId: Long =
			dslContext
				.insertInto(
					MEMBER,
					MEMBER.CERTIFICATION_ID,
					MEMBER.NICKNAME,
					MEMBER.EMAIL,
					MEMBER.PROFILE_IMAGE_URL,
					MEMBER.ACCOUNT_TYPE,
				).values(
					UUIDUtils.uuidToByteArray(member.certificationId),
					member.nickname,
					member.email?.address,
					member.profileImage,
					member.accountType.name,
				).returningResult(MEMBER.ID)
				.fetchOneInto(Long::class.java)!!

		insertRoles(member.roles, memberId)

		return findById(memberId)!!
	}

	private fun insertRoles(
		roles: Collection<Role>,
		memberId: Long,
	) {
		val roleInsertQuery =
			roles.map {
				dslContext
					.insertInto(
						MEMBER_ROLE,
						MEMBER_ROLE.MEMBER_ID,
						MEMBER_ROLE.NAME,
					).values(
						memberId,
						it.name,
					)
			}

		dslContext.batch(roleInsertQuery).execute()
	}

	private fun findById(id: Long): Member? {
		val record: Map<MemberRecord, Result<Record>> =
			dslContext
				.select()
				.from(MEMBER)
				.join(MEMBER_ROLE)
				.on(MEMBER_ROLE.MEMBER_ID.eq(MEMBER.ID))
				.where(MEMBER.ID.eq(id))
				.fetchGroups(MEMBER)

		return memberMapper.toMember(record)
	}

	fun update(member: Member) {
		dslContext
			.update(MEMBER)
			.set(MEMBER.NICKNAME, member.nickname)
			.set(MEMBER.EMAIL, member.email?.address)
			.set(MEMBER.PROFILE_IMAGE_URL, member.profileImage)
			.set(MEMBER.WITHDRAW, member.withdraw)
			.where(MEMBER.ID.eq(member.id))
			.execute()

		dslContext
			.deleteFrom(MEMBER_ROLE)
			.where(MEMBER_ROLE.MEMBER_ID.eq(member.id))
			.execute()

		insertRoles(roles = member.roles, memberId = member.id)
	}
}