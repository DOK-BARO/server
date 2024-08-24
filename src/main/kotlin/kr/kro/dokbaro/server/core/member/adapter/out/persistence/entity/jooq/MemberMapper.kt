package kr.kro.dokbaro.server.core.member.adapter.out.persistence.entity.jooq

import kr.kro.dokbaro.server.common.util.UUIDUtils
import kr.kro.dokbaro.server.core.member.domain.EmailAddress
import kr.kro.dokbaro.server.core.member.domain.Emails
import kr.kro.dokbaro.server.core.member.domain.Member
import kr.kro.dokbaro.server.core.member.domain.Role
import org.jooq.Record
import org.jooq.Result
import org.jooq.generated.tables.JMemberEmail
import org.jooq.generated.tables.JMemberRole
import org.jooq.generated.tables.records.MemberRecord
import org.springframework.stereotype.Component

@Component
class MemberMapper {
	companion object {
		private val MEMBER_ROLE = JMemberRole.MEMBER_ROLE
		private val MEMBER_EMAIL = JMemberEmail.MEMBER_EMAIL
	}

	fun mapTo(recordMap: Map<MemberRecord, Result<Record>>): Member? =
		recordMap
			.map { record ->
				Member(
					record.key.name,
					record.key.nickname,
					recordToMails(
						record.value
							.map { v ->
								MemberEmail(v.getValue(MEMBER_EMAIL.ADDRESS), v.getValue(MEMBER_EMAIL.MAIN))
							}.distinct(),
					),
					record.key.profileImageUrl,
					UUIDUtils.byteArrayToUUID(record.key.certificationId),
					record.value.map { Role.valueOf(it.getValue(MEMBER_ROLE.NAME)) }.toSet(),
					record.key.id,
				)
			}.firstOrNull()

	private fun recordToMails(emails: Collection<MemberEmail>): Emails? =
		emails.find { it.main }?.let { mainEmail ->
			Emails(
				EmailAddress(mainEmail.address),
				emails
					.filter { !it.main }
					.map { EmailAddress(it.address) }
					.toSet(),
			)
		}
}

data class MemberEmail(
	val address: String,
	val main: Boolean,
)