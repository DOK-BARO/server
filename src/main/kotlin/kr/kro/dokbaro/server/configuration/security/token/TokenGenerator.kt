package kr.kro.dokbaro.server.configuration.security.token

interface TokenGenerator {
	fun generate(token: TokenClaims): String
}