package kr.kro.dokbaro.server.security.jwt

import kr.kro.dokbaro.server.security.jwt.access.AccessTokenGenerator
import kr.kro.dokbaro.server.security.jwt.refresh.RefreshToken
import kr.kro.dokbaro.server.security.jwt.refresh.RefreshTokenGenerator
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class JwtTokenGenerator(
	private val accessTokenGenerator: AccessTokenGenerator,
	private val refreshTokenGenerator: RefreshTokenGenerator,
) {
	fun generate(certificationId: UUID): JwtResponse {
		val accessToken: String = accessTokenGenerator.generate(certificationId)
		val refreshToken: RefreshToken = refreshTokenGenerator.generate(certificationId)

		return JwtResponse(
			accessToken = accessToken,
			refreshToken = refreshToken.tokenValue,
		)
	}
}