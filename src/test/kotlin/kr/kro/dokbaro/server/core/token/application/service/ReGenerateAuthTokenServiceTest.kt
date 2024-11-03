package kr.kro.dokbaro.server.core.token.application.service

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldNotBe
import io.mockk.every
import io.mockk.mockk
import kr.kro.dokbaro.server.core.member.application.port.input.query.FindCertificatedMemberUseCase
import kr.kro.dokbaro.server.core.token.application.port.out.DeleteRefreshTokenPort
import kr.kro.dokbaro.server.core.token.application.port.out.InsertRefreshTokenPort
import kr.kro.dokbaro.server.core.token.application.port.out.LoadRefreshTokenPort
import kr.kro.dokbaro.server.core.token.application.port.out.UpdateRefreshTokenPort
import kr.kro.dokbaro.server.core.token.application.service.exception.CompromisedTokenException
import kr.kro.dokbaro.server.core.token.application.service.exception.ExpiredTokenException
import kr.kro.dokbaro.server.core.token.application.service.exception.NotFoundTokenException
import kr.kro.dokbaro.server.core.token.domain.access.jwt.AccessTokenGenerator
import kr.kro.dokbaro.server.fixture.domain.certificatedMemberFixture
import kr.kro.dokbaro.server.fixture.domain.refreshTokenFixture
import java.time.Clock
import java.time.LocalDateTime

class ReGenerateAuthTokenServiceTest :
	StringSpec({
		val insertRefreshTokenPort = mockk<InsertRefreshTokenPort>()
		val loadRefreshTokenPort = mockk<LoadRefreshTokenPort>()
		val updateRefreshTokenPort = mockk<UpdateRefreshTokenPort>()
		val deleteRefreshTokenPort = mockk<DeleteRefreshTokenPort>()
		val accessTokenGenerator = mockk<AccessTokenGenerator>()
		val refreshTokenGenerator = mockk<RefreshTokenGenerator>()
		val findCertificatedMemberUseCase = mockk<FindCertificatedMemberUseCase>()

		val clock = Clock.systemUTC()
		val reGenerateAuthTokenService =
			ReGenerateAuthTokenService(
				insertRefreshTokenPort,
				loadRefreshTokenPort,
				updateRefreshTokenPort,
				deleteRefreshTokenPort,
				accessTokenGenerator,
				refreshTokenGenerator,
				findCertificatedMemberUseCase,
				clock,
			)

		"토큰 재발급을 수행한다" {
			every { insertRefreshTokenPort.insert(any()) } returns Unit
			every { loadRefreshTokenPort.loadByToken(any()) } returns refreshTokenFixture()
			every { updateRefreshTokenPort.update(any()) } returns Unit
			every { deleteRefreshTokenPort.deleteBy(any()) } returns Unit
			every { accessTokenGenerator.generate(any()) } returns "access-token"
			every { refreshTokenGenerator.generate(any()) } returns refreshTokenFixture()
			every { findCertificatedMemberUseCase.getByCertificationId(any()) } returns certificatedMemberFixture()

			reGenerateAuthTokenService.reGenerate("refresh-token") shouldNotBe null
		}

		"토큰 미탐색 시 예외를 반환한다" {
			every { loadRefreshTokenPort.loadByToken(any()) } returns null

			shouldThrow<NotFoundTokenException> {
				reGenerateAuthTokenService.reGenerate("refresh-token")
			}
		}

		"재발급 시 토큰이 이미 사용되었으면 예외를 반환한다" {
			every { loadRefreshTokenPort.loadByToken(any()) } returns
				refreshTokenFixture(used = true)

			shouldThrow<CompromisedTokenException> {
				reGenerateAuthTokenService.reGenerate("refresh-token")
			}
		}

		"재발급 시 토큰이 만료되었으면 예외를 반환한다" {
			every { loadRefreshTokenPort.loadByToken(any()) } returns
				refreshTokenFixture(
					expiredAt = LocalDateTime.now(clock).minusDays(20),
				)
			every { deleteRefreshTokenPort.deleteBy(any()) } returns Unit

			shouldThrow<ExpiredTokenException> {
				reGenerateAuthTokenService.reGenerate("refresh-token")
			}
		}
	})