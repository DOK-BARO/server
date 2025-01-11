package kr.kro.dokbaro.server.core.member.adapter.out.persistence.entity.jooq

import kr.kro.dokbaro.server.common.annotation.Mapper
import kr.kro.dokbaro.server.common.util.UUIDUtils
import kr.kro.dokbaro.server.core.member.domain.AccountType
import kr.kro.dokbaro.server.core.member.domain.Email
import kr.kro.dokbaro.server.core.member.domain.Member
import kr.kro.dokbaro.server.core.member.domain.Role
import kr.kro.dokbaro.server.core.member.query.CertificatedMember
import kr.kro.dokbaro.server.core.member.query.EmailAuthenticationMember
import org.jooq.Record
import org.jooq.Result
import org.jooq.generated.tables.JEmailAccount.EMAIL_ACCOUNT
import org.jooq.generated.tables.JMember.MEMBER
import org.jooq.generated.tables.JMemberRole.MEMBER_ROLE
import org.jooq.generated.tables.records.MemberRecord

@Mapper
class MemberMapper {
	fun toMember(recordMap: Map<MemberRecord, Result<Record>>): Member? =
		recordMap
			.map { record ->
				Member(
					id = record.key.id,
					nickname = record.key.nickname,
					email = record.key.email?.let { Email(it) },
					profileImage = record.key.profileImageUrl,
					certificationId = UUIDUtils.byteArrayToUUID(record.key.certificationId),
					roles = record.value.map { Role.valueOf(it.getValue(MEMBER_ROLE.NAME)) }.toSet(),
					accountType = AccountType.valueOf(record.key.accountType),
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
					password = it[EMAIL_ACCOUNT.PASSWORD],
				)
			}.firstOrNull()
}