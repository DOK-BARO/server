package kr.kro.dokbaro.server.security.jwt.access

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import kr.kro.dokbaro.server.security.SecurityConstants
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.security.Key
import java.time.Clock
import java.time.ZonedDateTime
import java.util.Date
import java.util.UUID

@Component
class AccessTokenGenerator(
	private val key: Key,
	private val clock: Clock,
	@Value("\${jwt.limit-minute}") private val limitMinute: Long,
) {
	fun generate(certificationId: UUID): String {
		val claims =
			Jwts
				.claims()
				.add(SecurityConstants.JWT_CLAIM_ID, certificationId.toString())
				.build()

		return compact(claims)
	}

	private fun compact(claims: Claims): String {
		val now = ZonedDateTime.now(clock)

		return Jwts
			.builder()
			.claims(claims)
			.issuedAt(Date.from(now.toInstant()))
			.signWith(key)
			.expiration(Date.from(now.plusMinutes(limitMinute).toInstant()))
			.compact()
	}
}