package kr.kro.dokbaro.server.configuration.security.token

class AuthTokenGenerator(
	private val accessTokenGenerator: TokenGenerator,
	private val refreshTokenGenerator: TokenGenerator,
) {
	fun generate(claim: TokenClaims): AuthTokens {
		val accessToken = accessTokenGenerator.generate(claim)
		val refreshToken = refreshTokenGenerator.generate(claim)

		return AuthTokens(accessToken, refreshToken)
	}
}