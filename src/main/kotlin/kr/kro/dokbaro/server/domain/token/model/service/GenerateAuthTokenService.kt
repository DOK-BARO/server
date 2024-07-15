package kr.kro.dokbaro.server.domain.token.model.service

import kr.kro.dokbaro.server.domain.token.model.AuthToken
import kr.kro.dokbaro.server.domain.token.model.access.jwt.AccessTokenGenerator
import kr.kro.dokbaro.server.domain.token.model.access.jwt.TokenClaims
import kr.kro.dokbaro.server.domain.token.model.refresh.RefreshTokenGenerator
import kr.kro.dokbaro.server.domain.token.port.input.GenerateAuthTokenUseCase
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