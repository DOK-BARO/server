package kr.kro.dokbaro.server.configuration.security.token.refresh

import kr.kro.dokbaro.server.configuration.security.token.TokenClaims
import kr.kro.dokbaro.server.configuration.security.token.TokenGenerator
import java.util.UUID

class RefreshTokenGenerator : TokenGenerator {
	override fun generate(token: TokenClaims): String {
		return UUID.randomUUID().toString()
	}
}