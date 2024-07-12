package kr.kro.dokbaro.server.domain.token.model.service

import kr.kro.dokbaro.server.domain.account.port.input.query.AccountFinder
import kr.kro.dokbaro.server.domain.account.port.input.query.dto.AccountResponse
import kr.kro.dokbaro.server.domain.token.model.AuthToken
import kr.kro.dokbaro.server.domain.token.model.access.jwt.AccessTokenGenerator
import kr.kro.dokbaro.server.domain.token.model.access.jwt.TokenClaims
import kr.kro.dokbaro.server.domain.token.model.refresh.RefreshTokenGenerator
import kr.kro.dokbaro.server.domain.token.port.input.GenerateAuthTokenUseCase
import org.springframework.stereotype.Service

@Service
class GenerateAuthTokenService(
	private val accountFinder: AccountFinder,
	private val accessTokenGenerator: AccessTokenGenerator,
	private val refreshTokenGenerator: RefreshTokenGenerator,
) : GenerateAuthTokenUseCase {
	override fun generate(accountId: String): AuthToken {
		val account: AccountResponse = accountFinder.getById(accountId)
		val accessToken: String = accessTokenGenerator.generate(TokenClaims(account.id.toString(), account.role))
		val refreshToken: String = refreshTokenGenerator.generate(accountId)

		return AuthToken(accessToken, refreshToken)
	}
}