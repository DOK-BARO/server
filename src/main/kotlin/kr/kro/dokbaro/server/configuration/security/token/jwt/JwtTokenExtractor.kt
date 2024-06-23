package kr.kro.dokbaro.server.configuration.security.token.jwt

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import kr.kro.dokbaro.server.configuration.security.token.TokenClaims
import kr.kro.dokbaro.server.configuration.security.token.TokenExtractor
import java.security.Key
import javax.crypto.SecretKey

class JwtTokenExtractor(
	private val key: Key,
) : TokenExtractor {
	@Suppress("UNCHECKED_CAST")
	override fun extract(token: String): TokenClaims {
		val claims: Claims = parseClaims(token)
		return TokenClaims(
			claims["id", String::class.java],
			claims["role", ArrayList::class.java] as ArrayList<String>,
		)
	}

	private fun parseClaims(token: String): Claims =
		Jwts
			.parser()
			.verifyWith(key as SecretKey)
			.build()
			.parseSignedClaims(token)
			.payload
}