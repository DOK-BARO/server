package kr.kro.dokbaro.server.configuration.security.token

interface TokenExtractor {
	fun extract(token: String): TokenClaims
}