package kr.kro.dokbaro.server.core.token.application.service

import kr.kro.dokbaro.server.common.util.UUIDUtils
import kr.kro.dokbaro.server.core.token.application.port.input.GenerateAuthTokenUseCase
import kr.kro.dokbaro.server.core.token.application.port.out.DeleteRefreshTokenPort
import kr.kro.dokbaro.server.core.token.application.port.out.InsertRefreshTokenPort
import kr.kro.dokbaro.server.core.token.domain.AuthToken
import kr.kro.dokbaro.server.core.token.domain.access.jwt.AccessTokenGenerator
import kr.kro.dokbaro.server.core.token.domain.access.jwt.TokenClaims
import kr.kro.dokbaro.server.core.token.domain.refresh.RefreshToken
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class GenerateAuthTokenService(
	private val accessTokenGenerator: AccessTokenGenerator,
	private val insertRefreshTokenPort: InsertRefreshTokenPort,
	private val refreshTokenGenerator: RefreshTokenGenerator,
	private val deleteRefreshTokenPort: DeleteRefreshTokenPort,
) : GenerateAuthTokenUseCase {
	override fun generate(claims: TokenClaims): AuthToken {
		val accessToken: String = accessTokenGenerator.generate(claims)
		val certificateId: UUID = UUIDUtils.stringToUUID(claims.id)

		val refreshToken: RefreshToken = refreshTokenGenerator.generate(certificateId)
		deleteRefreshTokenPort.deleteBy(certificateId)
		insertRefreshTokenPort.insert(refreshToken)

		return AuthToken(accessToken, refreshToken.token)
	}
}