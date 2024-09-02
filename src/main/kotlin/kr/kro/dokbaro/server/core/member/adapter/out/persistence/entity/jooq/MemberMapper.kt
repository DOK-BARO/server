package kr.kro.dokbaro.server.core.member.adapter.out.persistence.entity.jooq

import kr.kro.dokbaro.server.common.util.UUIDUtils
import kr.kro.dokbaro.server.core.member.domain.Email
import kr.kro.dokbaro.server.core.member.domain.Member
import kr.kro.dokbaro.server.core.member.domain.Role
import org.jooq.Record
import org.jooq.Result
import org.jooq.generated.tables.JMemberRole
import org.jooq.generated.tables.records.MemberRecord
import org.springframework.stereotype.Component

@Component
class MemberMapper {
	companion object {
		private val MEMBER_ROLE = JMemberRole.MEMBER_ROLE
	}

	fun mapTo(recordMap: Map<MemberRecord, Result<Record>>): Member? =
		recordMap
			.map { record ->
				Member(
					record.key.nickname,
					Email(record.key.email),
					record.key.profileImageUrl,
					UUIDUtils.byteArrayToUUID(record.key.certificationId),
					record.value.map { Role.valueOf(it.getValue(MEMBER_ROLE.NAME)) }.toSet(),
					record.key.id,
				)
			}.firstOrNull()
}