package kr.kro.dokbaro.server.domain.token.refresh

import kr.kro.dokbaro.server.domain.token.TokenClaims
import kr.kro.dokbaro.server.domain.token.TokenGenerator
import java.util.UUID

class RefreshTokenGenerator : TokenGenerator {
	override fun generate(token: TokenClaims): String = UUID.randomUUID().toString()
}