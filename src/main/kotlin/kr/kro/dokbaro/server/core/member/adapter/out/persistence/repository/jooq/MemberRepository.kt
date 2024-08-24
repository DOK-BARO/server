package kr.kro.dokbaro.server.core.member.adapter.out.persistence.repository.jooq

import kr.kro.dokbaro.server.common.util.UUIDUtils
import kr.kro.dokbaro.server.core.member.domain.Emails
import kr.kro.dokbaro.server.core.member.domain.Member
import kr.kro.dokbaro.server.core.member.domain.Role
import org.jooq.DSLContext
import org.jooq.generated.tables.JMember
import org.jooq.generated.tables.JMemberEmail
import org.jooq.generated.tables.JMemberRole
import org.springframework.stereotype.Repository

@Repository
class MemberRepository(
	private val dslContext: DSLContext,
) {
	companion object {
		private val MEMBER = JMember.MEMBER
		private val MEMBER_ROLE = JMemberRole.MEMBER_ROLE
		private val MEMBER_EMAIL = JMemberEmail.MEMBER_EMAIL
	}

	fun save(member: Member) {
		val memberId: Long =
			dslContext
				.insertInto(
					MEMBER,
					MEMBER.NAME,
					MEMBER.NICKNAME,
					MEMBER.CERTIFICATION_ID,
				).values(
					member.name,
					member.nickname,
					UUIDUtils.uuidToByteArray(member.certificationId),
				).returningResult(MEMBER.ID)
				.fetchOneInto(Long::class.java)!!

		insertRoles(member.roles, memberId)
		member.emails?.let {
			insertEmails(it, memberId)
		}
	}

	private fun insertRoles(
		roles: Collection<Role>,
		memberId: Long,
	) {
		roles.map { it.name }.forEach {
			dslContext
				.insertInto(
					MEMBER_ROLE,
					MEMBER_ROLE.MEMBER_ID,
					MEMBER_ROLE.NAME,
				).values(
					memberId,
					it,
				).execute()
		}
	}

	private fun insertEmails(
		emails: Emails,
		memberId: Long,
	) {
		dslContext
			.insertInto(
				MEMBER_EMAIL,
				MEMBER_EMAIL.MEMBER_ID,
				MEMBER_EMAIL.ADDRESS,
				MEMBER_EMAIL.MAIN,
			).values(
				memberId,
				emails.mainEmail.value,
				true,
			).execute()

		emails.subEmail.forEach {
			dslContext
				.insertInto(
					MEMBER_EMAIL,
					MEMBER_EMAIL.MEMBER_ID,
					MEMBER_EMAIL.ADDRESS,
					MEMBER_EMAIL.MAIN,
				).values(
					memberId,
					it.value,
					false,
				).execute()
		}
	}
}