package kr.kro.dokbaro.server.security.jwt.refresh

import java.util.UUID

interface RefreshTokenRepository {
	fun insert(refreshToken: RefreshToken)

	fun findByTokenValue(tokenValue: String): RefreshToken?

	fun update(refreshToken: RefreshToken)

	fun deleteByCertificationId(certificationId: UUID)
}