package kr.kro.dokbaro.server.domain.token.model.access.jwt

import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class JwtTest :
	StringSpec({
		val key =
			Keys.hmacShaKeyFor(Decoders.BASE64.decode("asdfdjskdjaksfasjkwqersdfzcxvasdfdjskdjaksfasjkwqersdfzcxv"))
		val accessTokenGenerator = AccessTokenGenerator(key)
		val accessTokenDecoder = AccessTokenDecoder(key)

		"jwt token 생성 및 추출을 수행한다" {
			val tokenClaims = TokenClaims("account", setOf("ADMIN", "USER"))

			val accessKey = accessTokenGenerator.generate(tokenClaims)

			val decodeResult: TokenClaims = accessTokenDecoder.decode(accessKey)

			decodeResult.id shouldBe tokenClaims.id
			decodeResult.role shouldBe tokenClaims.role
		}
	})