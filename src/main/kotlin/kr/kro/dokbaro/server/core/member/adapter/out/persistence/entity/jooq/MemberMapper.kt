package kr.kro.dokbaro.server.core.member.adapter.out.persistence.entity.jooq

import kr.kro.dokbaro.server.common.annotation.Mapper
import kr.kro.dokbaro.server.common.util.UUIDUtils
import kr.kro.dokbaro.server.core.member.domain.Email
import kr.kro.dokbaro.server.core.member.domain.Member
import kr.kro.dokbaro.server.core.member.domain.Role
import kr.kro.dokbaro.server.core.member.query.CertificatedMember
import kr.kro.dokbaro.server.core.member.query.EmailAuthenticationMember
import org.jooq.Record
import org.jooq.Result
import org.jooq.generated.tables.JAccountPassword
import org.jooq.generated.tables.JMember
import org.jooq.generated.tables.JMemberRole
import org.jooq.generated.tables.records.MemberRecord

@Mapper
class MemberMapper {
	companion object {
		private val MEMBER = JMember.MEMBER
		private val ACCOUNT_PASSWORD = JAccountPassword.ACCOUNT_PASSWORD
		private val MEMBER_ROLE = JMemberRole.MEMBER_ROLE
	}

	fun toMember(recordMap: Map<MemberRecord, Result<Record>>): Member? =
		recordMap
			.map { record ->
				Member(
					id = record.key.id,
					nickname = record.key.nickname,
					email = Email(record.key.email),
					profileImage = record.key.profileImageUrl,
					certificationId = UUIDUtils.byteArrayToUUID(record.key.certificationId),
					roles = record.value.map { Role.valueOf(it.getValue(MEMBER_ROLE.NAME)) }.toSet(),
				)
			}.firstOrNull()

	fun toCertificatedMember(record: Result<out Record>): CertificatedMember? =
		record
			.map {
				CertificatedMember(
					id = it[MEMBER.ID],
					certificationId = UUIDUtils.byteArrayToUUID(it[MEMBER.CERTIFICATION_ID]),
					nickname = it[MEMBER.NICKNAME],
					email = it[MEMBER.EMAIL],
					role = record.map { v -> v[MEMBER_ROLE.NAME] },
				)
			}.firstOrNull()

	fun toEmailAuthenticationMember(record: Result<out Record>): EmailAuthenticationMember? =
		record
			.map {
				EmailAuthenticationMember(
					id = it[MEMBER.ID],
					certificationId = UUIDUtils.byteArrayToUUID(it[MEMBER.CERTIFICATION_ID]),
					nickname = it[MEMBER.NICKNAME],
					email = it[MEMBER.EMAIL],
					role = record.map { v -> v[MEMBER_ROLE.NAME] },
					password = it[ACCOUNT_PASSWORD.PASSWORD],
				)
			}.firstOrNull()
}