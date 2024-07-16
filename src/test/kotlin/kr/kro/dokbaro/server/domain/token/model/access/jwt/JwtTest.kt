package kr.kro.dokbaro.server.domain.token.model.access.jwt

import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import java.time.Clock
import java.time.Instant
import java.time.ZoneOffset

class JwtTest :
	StringSpec({
		val key =
			Keys.hmacShaKeyFor(Decoders.BASE64.decode("asdfdjskdjaksfasjkwqersdfzcxvasdfdjskdjaksfasjkwqersdfzcxv"))
		val accessTokenDecoder = AccessTokenDecoder(key)
		val baseClock = Clock.fixed(Instant.now(), ZoneOffset.UTC)
		val accessTokenGenerator = AccessTokenGenerator(key, baseClock, 15)
		val tokenClaims = TokenClaims("account", setOf("ADMIN", "USER"))

		"jwt token 생성 및 추출을 수행한다" {
			val accessToken = accessTokenGenerator.generate(tokenClaims)
			val decodeResult: TokenClaims = accessTokenDecoder.decode(accessToken)

			decodeResult.id shouldBe tokenClaims.id
			decodeResult.role shouldBe tokenClaims.role
		}

		"만로된 토큰 추출 시 예외를 반환한다" {
			val expiredTokenGenerator =
				AccessTokenGenerator(key, Clock.fixed(Instant.now().minusSeconds(16 * 60), ZoneOffset.UTC), 15)

			val accessToken = expiredTokenGenerator.generate(tokenClaims)

			shouldThrow<ExpiredJwtException> {
				accessTokenDecoder.decode(accessToken)
			}
		}
	})