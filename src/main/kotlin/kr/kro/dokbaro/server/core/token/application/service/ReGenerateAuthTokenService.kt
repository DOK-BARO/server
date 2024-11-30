package kr.kro.dokbaro.server.core.token.application.service

import kr.kro.dokbaro.server.common.util.UUIDUtils
import kr.kro.dokbaro.server.core.member.application.port.input.query.FindCertificatedMemberUseCase
import kr.kro.dokbaro.server.core.token.application.port.input.ReGenerateAuthTokenUseCase
import kr.kro.dokbaro.server.core.token.application.port.out.DeleteRefreshTokenPort
import kr.kro.dokbaro.server.core.token.application.port.out.InsertRefreshTokenPort
import kr.kro.dokbaro.server.core.token.application.port.out.LoadRefreshTokenPort
import kr.kro.dokbaro.server.core.token.application.port.out.UpdateRefreshTokenPort
import kr.kro.dokbaro.server.core.token.application.service.exception.CompromisedTokenException
import kr.kro.dokbaro.server.core.token.application.service.exception.ExpiredTokenException
import kr.kro.dokbaro.server.core.token.application.service.exception.NotFoundTokenException
import kr.kro.dokbaro.server.core.token.domain.AuthToken
import kr.kro.dokbaro.server.core.token.domain.access.jwt.AccessTokenGenerator
import kr.kro.dokbaro.server.core.token.domain.access.jwt.TokenClaims
import kr.kro.dokbaro.server.core.token.domain.refresh.RefreshToken
import org.springframework.stereotype.Service
import java.time.Clock

@Service
class ReGenerateAuthTokenService(
	private val insertRefreshTokenPort: InsertRefreshTokenPort,
	private val loadRefreshTokenPort: LoadRefreshTokenPort,
	private val updateRefreshTokenPort: UpdateRefreshTokenPort,
	private val deleteRefreshTokenPort: DeleteRefreshTokenPort,
	private val accessTokenGenerator: AccessTokenGenerator,
	private val refreshTokenGenerator: RefreshTokenGenerator,
	private val findCertificatedMemberUseCase: FindCertificatedMemberUseCase,
	private val clock: Clock,
) : ReGenerateAuthTokenUseCase {
	override fun reGenerate(refreshToken: String): AuthToken {
		val nowToken: RefreshToken = loadRefreshTokenPort.loadByToken(refreshToken) ?: throw NotFoundTokenException()
		val certificateId = nowToken.certificateId

		if (nowToken.used) {
			deleteRefreshTokenPort.deleteBy(certificateId)
			throw CompromisedTokenException()
		}

		if (nowToken.isExpired(clock)) {
			throw ExpiredTokenException()
		}

		nowToken.use()
		updateRefreshTokenPort.update(nowToken)

		val newToken: RefreshToken = refreshTokenGenerator.generate(certificateId)
		insertRefreshTokenPort.insert(newToken)

		return AuthToken(
			accessToken =
				accessTokenGenerator.generate(
					TokenClaims(
						id = UUIDUtils.uuidToString(certificateId),
						role =
							findCertificatedMemberUseCase
								.getByCertificationId(certificateId)
								.roles
								.map { it.name },
					),
				),
			refreshToken = newToken.token,
		)
	}
}