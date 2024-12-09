package kr.kro.dokbaro.server.security.jwt.refresh.persistence

import kr.kro.dokbaro.server.common.annotation.Mapper
import kr.kro.dokbaro.server.common.util.UUIDUtils
import kr.kro.dokbaro.server.security.jwt.refresh.RefreshToken
import org.jooq.generated.tables.records.RefreshTokenRecord

@Mapper
class RefreshTokenMapper {
	fun toRefreshToken(record: RefreshTokenRecord?): RefreshToken? =
		record?.let {
			RefreshToken(
				tokenValue = it.tokenValue,
				certificationId = UUIDUtils.byteArrayToUUID(it.memberCertificationId),
				expiredAt = it.expiredAt,
				used = it.used,
			)
		}
}