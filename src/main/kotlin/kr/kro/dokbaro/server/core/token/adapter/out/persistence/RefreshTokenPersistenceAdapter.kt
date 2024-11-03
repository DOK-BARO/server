package kr.kro.dokbaro.server.core.token.adapter.out.persistence

import kr.kro.dokbaro.server.common.annotation.PersistenceAdapter
import kr.kro.dokbaro.server.core.token.adapter.out.persistence.repository.RefreshTokenRepository
import kr.kro.dokbaro.server.core.token.application.port.out.DeleteRefreshTokenPort
import kr.kro.dokbaro.server.core.token.application.port.out.InsertRefreshTokenPort
import kr.kro.dokbaro.server.core.token.application.port.out.LoadRefreshTokenPort
import kr.kro.dokbaro.server.core.token.application.port.out.UpdateRefreshTokenPort
import kr.kro.dokbaro.server.core.token.domain.refresh.RefreshToken
import java.util.UUID

@PersistenceAdapter
class RefreshTokenPersistenceAdapter(
	private val refreshTokenRepository: RefreshTokenRepository,
) : InsertRefreshTokenPort,
	LoadRefreshTokenPort,
	UpdateRefreshTokenPort,
	DeleteRefreshTokenPort {
	override fun insert(token: RefreshToken) = refreshTokenRepository.insert(token)

	override fun loadByToken(token: String): RefreshToken? = refreshTokenRepository.findByTokenValue(token)

	override fun update(refreshToken: RefreshToken) {
		refreshTokenRepository.update(refreshToken)
	}

	override fun deleteBy(certificateId: UUID) {
		refreshTokenRepository.deleteBy(certificateId)
	}
}