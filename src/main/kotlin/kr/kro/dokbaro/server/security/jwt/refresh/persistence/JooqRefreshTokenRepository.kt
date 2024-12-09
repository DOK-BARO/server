package kr.kro.dokbaro.server.security.jwt.refresh.persistence

import kr.kro.dokbaro.server.common.util.UUIDUtils
import kr.kro.dokbaro.server.security.jwt.refresh.RefreshToken
import kr.kro.dokbaro.server.security.jwt.refresh.RefreshTokenRepository
import org.jooq.DSLContext
import org.jooq.generated.tables.JRefreshToken
import org.jooq.generated.tables.records.RefreshTokenRecord
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class JooqRefreshTokenRepository(
	private val dslContext: DSLContext,
	private val refreshTokenMapper: RefreshTokenMapper,
) : RefreshTokenRepository {
	companion object {
		private val REFRESH_TOKEN = JRefreshToken.REFRESH_TOKEN
	}

	override fun insert(refreshToken: RefreshToken) {
		dslContext
			.insertInto(
				REFRESH_TOKEN,
				REFRESH_TOKEN.TOKEN_VALUE,
				REFRESH_TOKEN.MEMBER_CERTIFICATION_ID,
				REFRESH_TOKEN.USED,
				REFRESH_TOKEN.EXPIRED_AT,
			).values(
				refreshToken.tokenValue,
				UUIDUtils.uuidToByteArray(refreshToken.certificationId),
				refreshToken.used,
				refreshToken.expiredAt,
			).execute()
	}

	override fun findByTokenValue(tokenValue: String): RefreshToken? {
		val record: RefreshTokenRecord? =
			dslContext
				.select(
					REFRESH_TOKEN.TOKEN_VALUE,
					REFRESH_TOKEN.MEMBER_CERTIFICATION_ID,
					REFRESH_TOKEN.USED,
					REFRESH_TOKEN.EXPIRED_AT,
				).from(REFRESH_TOKEN)
				.where(REFRESH_TOKEN.TOKEN_VALUE.eq(tokenValue))
				.fetchOneInto(REFRESH_TOKEN)

		return refreshTokenMapper.toRefreshToken(record)
	}

	override fun update(refreshToken: RefreshToken) {
		dslContext
			.update(REFRESH_TOKEN)
			.set(REFRESH_TOKEN.USED, refreshToken.used)
			.where(REFRESH_TOKEN.TOKEN_VALUE.eq(refreshToken.tokenValue))
			.execute()
	}

	override fun deleteByCertificationId(certificationId: UUID) {
		dslContext
			.deleteFrom(REFRESH_TOKEN)
			.where(REFRESH_TOKEN.MEMBER_CERTIFICATION_ID.eq(UUIDUtils.uuidToByteArray(certificationId)))
			.execute()
	}
}