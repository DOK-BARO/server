package kr.kro.dokbaro.server.security.jwt.access

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import kr.kro.dokbaro.server.security.SecurityConstants
import org.springframework.stereotype.Component
import java.security.Key
import java.util.UUID
import javax.crypto.SecretKey

@Component
class AccessTokenDecoder(
	private val key: Key,
) {
	fun decode(token: String): UUID {
		val claims: Claims = parseClaims(token)

		return UUID.fromString(claims[SecurityConstants.JWT_CLAIM_ID].toString())
	}

	private fun parseClaims(token: String): Claims =
		Jwts
			.parser()
			.verifyWith(key as SecretKey)
			.build()
			.parseSignedClaims(token)
			.payload
}