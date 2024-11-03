package kr.kro.dokbaro.server.core.token.adapter.out.persistence.entity

import kr.kro.dokbaro.server.common.annotation.Mapper
import kr.kro.dokbaro.server.common.util.UUIDUtils
import kr.kro.dokbaro.server.core.token.domain.refresh.RefreshToken
import org.jooq.generated.tables.records.RefreshTokenRecord

@Mapper
class RefreshTokenMapper {
	fun toRefreshToken(record: RefreshTokenRecord?): RefreshToken? =
		record?.let {
			RefreshToken(
				it.tokenValue,
				UUIDUtils.byteArrayToUUID(it.memberCertificationId),
				it.expiredAt,
				it.used,
			)
		}
}