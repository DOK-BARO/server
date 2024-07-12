package kr.kro.dokbaro.server.domain.token.model.service

import kr.kro.dokbaro.server.domain.token.model.access.jwt.AccessTokenDecoder
import kr.kro.dokbaro.server.domain.token.model.access.jwt.TokenClaims
import kr.kro.dokbaro.server.domain.token.port.input.DecodeAccessTokenUseCase
import org.springframework.stereotype.Service

@Service
class DecodeAccessTokenService(
	private val accessTokenDecoder: AccessTokenDecoder,
) : DecodeAccessTokenUseCase {
	override fun decode(accessToken: String): TokenClaims = accessTokenDecoder.decode(accessToken)
}