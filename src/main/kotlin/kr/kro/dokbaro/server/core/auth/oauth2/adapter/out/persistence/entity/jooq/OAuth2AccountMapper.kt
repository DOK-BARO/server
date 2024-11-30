package kr.kro.dokbaro.server.core.auth.oauth2.adapter.out.persistence.entity.jooq

import kr.kro.dokbaro.server.common.annotation.Mapper
import kr.kro.dokbaro.server.common.util.UUIDUtils
import kr.kro.dokbaro.server.core.auth.oauth2.domain.OAuth2CertificatedAccount
import org.jooq.Result
import org.jooq.generated.tables.records.MemberRecord
import org.jooq.generated.tables.records.MemberRoleRecord

@Mapper
class OAuth2AccountMapper {
	fun toOAuthCertificatedAccount(record: Map<MemberRecord, Result<MemberRoleRecord>>): OAuth2CertificatedAccount? =
		record
			.map {
				OAuth2CertificatedAccount(
					certificationId = UUIDUtils.byteArrayToUUID(it.key.certificationId),
					role = it.value.map { v -> v.name }.toSet(),
				)
			}.firstOrNull()
}