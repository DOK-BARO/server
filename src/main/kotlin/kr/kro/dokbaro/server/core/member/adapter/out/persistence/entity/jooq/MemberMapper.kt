package kr.kro.dokbaro.server.core.member.adapter.out.persistence.entity.jooq

import kr.kro.dokbaro.server.common.annotation.Mapper
import kr.kro.dokbaro.server.common.util.UUIDUtils
import kr.kro.dokbaro.server.core.member.domain.Email
import kr.kro.dokbaro.server.core.member.domain.Member
import kr.kro.dokbaro.server.core.member.domain.Role
import org.jooq.Record
import org.jooq.Result
import org.jooq.generated.tables.JMemberRole
import org.jooq.generated.tables.records.MemberRecord

@Mapper
class MemberMapper {
	companion object {
		private val MEMBER_ROLE = JMemberRole.MEMBER_ROLE
	}

	fun toMember(recordMap: Map<MemberRecord, Result<Record>>): Member? =
		recordMap
			.map { record ->
				Member(
					id = record.key.id,
					nickName = record.key.nickname,
					email = Email(record.key.email),
					profileImage = record.key.profileImageUrl,
					certificationId = UUIDUtils.byteArrayToUUID(record.key.certificationId),
					roles = record.value.map { Role.valueOf(it.getValue(MEMBER_ROLE.NAME)) }.toSet(),
				)
			}.firstOrNull()
}