package kr.kro.dokbaro.server.security.jwt

import kr.kro.dokbaro.server.security.jwt.exception.ExpiredRefreshTokenException
import kr.kro.dokbaro.server.security.jwt.exception.NotFoundRefreshTokenException
import kr.kro.dokbaro.server.security.jwt.refresh.RefreshToken
import kr.kro.dokbaro.server.security.jwt.refresh.RefreshTokenRepository
import org.springframework.stereotype.Component
import java.time.Clock

@Component
class JwtTokenReGenerator(
	private val clock: Clock,
	private val refreshTokenRepository: RefreshTokenRepository,
	private val jwtTokenGenerator: JwtTokenGenerator,
) {
	fun reGenerate(refreshTokenValue: String): JwtResponse {
		val refreshToken: RefreshToken =
			refreshTokenRepository.findByTokenValue(refreshTokenValue)
				?: throw NotFoundRefreshTokenException()

		if (refreshToken.isExpired(clock)) {
			throw ExpiredRefreshTokenException()
		}

		refreshToken.use()
		refreshTokenRepository.update(refreshToken)

		return jwtTokenGenerator.generate(refreshToken.certificationId)
	}
}