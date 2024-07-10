package kr.kro.dokbaro.server.domain.token

interface TokenExtractor {
	fun extract(token: String): TokenClaims
}