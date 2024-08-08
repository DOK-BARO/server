package kr.kro.dokbaro.server.core.token.application.service

import kr.kro.dokbaro.server.core.token.application.port.input.DecodeAccessTokenUseCase
import kr.kro.dokbaro.server.core.token.domain.access.jwt.AccessTokenDecoder
import kr.kro.dokbaro.server.core.token.domain.access.jwt.TokenClaims
import org.springframework.stereotype.Service

@Service
class DecodeAccessTokenService(
	private val accessTokenDecoder: AccessTokenDecoder,
) : DecodeAccessTokenUseCase {
	override fun decode(accessToken: String): TokenClaims = accessTokenDecoder.decode(accessToken)
}