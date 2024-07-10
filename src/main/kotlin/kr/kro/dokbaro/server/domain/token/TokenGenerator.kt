package kr.kro.dokbaro.server.domain.token

interface TokenGenerator {
	fun generate(token: TokenClaims): String
}