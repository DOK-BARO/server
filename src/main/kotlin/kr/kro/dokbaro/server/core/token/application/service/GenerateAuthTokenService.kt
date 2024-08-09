package kr.kro.dokbaro.server.core.token.application.service

import kr.kro.dokbaro.server.core.token.application.port.input.GenerateAuthTokenUseCase
import kr.kro.dokbaro.server.core.token.domain.AuthToken
import kr.kro.dokbaro.server.core.token.domain.access.jwt.AccessTokenGenerator
import kr.kro.dokbaro.server.core.token.domain.access.jwt.TokenClaims
import kr.kro.dokbaro.server.core.token.domain.refresh.RefreshTokenGenerator
import org.springframework.stereotype.Service

@Service
class GenerateAuthTokenService(
	private val accessTokenGenerator: AccessTokenGenerator,
	private val refreshTokenGenerator: RefreshTokenGenerator,
) : GenerateAuthTokenUseCase {
	override fun generate(claims: TokenClaims): AuthToken {
		val accessToken: String = accessTokenGenerator.generate(claims)
		val refreshToken: String = refreshTokenGenerator.generate(claims.id)

		return AuthToken(accessToken, refreshToken)
	}
}