package kr.kro.dokbaro.server.domain.token.model.access.jwt

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import org.springframework.stereotype.Component
import java.security.Key

@Component
class AccessTokenGenerator(
	private val key: Key,
) {
	fun generate(token: TokenClaims): String {
		val claims = mapToClaims(token)
		return compact(claims)
	}

	private fun mapToClaims(claim: TokenClaims): Claims =
		Jwts
			.claims()
			.add("id", claim.id)
			.add("role", claim.role)
			.build()

	private fun compact(claims: Claims): String =
		Jwts
			.builder()
			.claims(claims)
			.signWith(key)
			.compact()
}