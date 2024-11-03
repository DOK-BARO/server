package kr.kro.dokbaro.server.core.token.adapter.out.persistence.repository

import kr.kro.dokbaro.server.common.util.UUIDUtils
import kr.kro.dokbaro.server.core.token.adapter.out.persistence.entity.RefreshTokenMapper
import kr.kro.dokbaro.server.core.token.domain.refresh.RefreshToken
import org.jooq.DSLContext
import org.jooq.generated.tables.JRefreshToken
import org.jooq.generated.tables.records.RefreshTokenRecord
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class RefreshTokenRepository(
	private val dslContext: DSLContext,
	private val refreshTokenMapper: RefreshTokenMapper,
) {
	companion object {
		private val REFRESH_TOKEN = JRefreshToken.REFRESH_TOKEN
	}

	fun insert(token: RefreshToken) {
		dslContext
			.insertInto(
				REFRESH_TOKEN,
				REFRESH_TOKEN.TOKEN_VALUE,
				REFRESH_TOKEN.MEMBER_CERTIFICATION_ID,
				REFRESH_TOKEN.EXPIRED_AT,
			).values(
				token.token,
				UUIDUtils.uuidToByteArray(token.certificateId),
				token.expiredAt,
			).execute()
	}

	fun findByTokenValue(token: String): RefreshToken? {
		val record: RefreshTokenRecord? =
			dslContext
				.select(
					REFRESH_TOKEN.TOKEN_VALUE,
					REFRESH_TOKEN.MEMBER_CERTIFICATION_ID,
					REFRESH_TOKEN.USED,
					REFRESH_TOKEN.EXPIRED_AT,
				).from(REFRESH_TOKEN)
				.where(REFRESH_TOKEN.TOKEN_VALUE.eq(token))
				.fetchOneInto(REFRESH_TOKEN)

		return refreshTokenMapper.toRefreshToken(record)
	}

	fun update(refreshToken: RefreshToken) {
		dslContext
			.update(REFRESH_TOKEN)
			.set(REFRESH_TOKEN.USED, refreshToken.used)
			.where(REFRESH_TOKEN.TOKEN_VALUE.eq(refreshToken.token))
			.execute()
	}

	fun deleteBy(certificateId: UUID) {
		dslContext
			.deleteFrom(REFRESH_TOKEN)
			.where(REFRESH_TOKEN.MEMBER_CERTIFICATION_ID.eq(UUIDUtils.uuidToByteArray(certificateId)))
			.execute()
	}
}