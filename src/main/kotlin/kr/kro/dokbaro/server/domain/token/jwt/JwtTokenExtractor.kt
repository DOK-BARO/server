package kr.kro.dokbaro.server.domain.token.jwt

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import kr.kro.dokbaro.server.domain.token.TokenClaims
import kr.kro.dokbaro.server.domain.token.TokenExtractor
import java.security.Key
import javax.crypto.SecretKey

class JwtTokenExtractor(
	private val key: Key,
) : TokenExtractor {
	override fun extract(token: String): TokenClaims {
		val claims: Claims = parseClaims(token)
		val attributes: Map<String, Any> =
			claims.mapValues { it.value.javaClass }

		return TokenClaims.from(attributes)
	}

	private fun parseClaims(token: String): Claims =
		Jwts
			.parser()
			.verifyWith(key as SecretKey)
			.build()
			.parseSignedClaims(token)
			.payload
}