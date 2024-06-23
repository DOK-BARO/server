package kr.kro.dokbaro.server.configuration.security.token.jwt

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import kr.kro.dokbaro.server.configuration.security.token.TokenClaims
import kr.kro.dokbaro.server.configuration.security.token.TokenGenerator
import java.security.Key

class JwtTokenGenerator(
	private val key: Key,
) : TokenGenerator {
	override fun generate(token: TokenClaims): String {
		val claims = mapToClaims(token)
		return compact(claims)
	}

	private fun mapToClaims(claim: TokenClaims): Claims {
		return Jwts.claims()
			.add("id", claim.id)
			.add("role", claim.role)
			.build()
	}

	private fun compact(claims: Claims): String =
		Jwts.builder()
			.claims(claims)
			.signWith(key)
			.compact()
}