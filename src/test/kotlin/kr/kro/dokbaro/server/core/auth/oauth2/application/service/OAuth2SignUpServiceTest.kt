package kr.kro.dokbaro.server.core.auth.oauth2.application.service

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import kr.kro.dokbaro.server.common.type.AuthProvider
import kr.kro.dokbaro.server.core.auth.oauth2.application.port.dto.LoadProviderAccountCommand
import kr.kro.dokbaro.server.core.auth.oauth2.application.port.out.ExistOAuth2AccountPort
import kr.kro.dokbaro.server.core.auth.oauth2.application.port.out.InsertOAuth2AccountPort
import kr.kro.dokbaro.server.core.auth.oauth2.application.service.exception.AlreadyExistAccountException
import kr.kro.dokbaro.server.core.member.application.port.input.command.RegisterMemberUseCase
import kr.kro.dokbaro.server.core.token.application.port.input.GenerateAuthTokenUseCase
import kr.kro.dokbaro.server.fixture.domain.authTokenFixture
import kr.kro.dokbaro.server.fixture.domain.memberFixture
import kr.kro.dokbaro.server.fixture.domain.oAuth2ProviderAccountFixture

class OAuth2SignUpServiceTest :
	StringSpec({
		val accountLoader = mockk<OAuth2ProviderAccountLoader>()
		val registerMemberUseCase = mockk<RegisterMemberUseCase>()
		val existOAuth2AccountPort = mockk<ExistOAuth2AccountPort>()
		val insertOAuth2AccountPort = mockk<InsertOAuth2AccountPort>()
		val generateAuthTokenUseCase = mockk<GenerateAuthTokenUseCase>()

		val oAuth2SignUpService =
			OAuth2SignUpService(
				accountLoader,
				registerMemberUseCase,
				existOAuth2AccountPort,
				insertOAuth2AccountPort,
				generateAuthTokenUseCase,
			)

		"회원가입을 수행한다" {
			every { accountLoader.getAccount(any()) } returns oAuth2ProviderAccountFixture()

			every { generateAuthTokenUseCase.generate(any()) } returns authTokenFixture()

			every { registerMemberUseCase.register(any()) } returns memberFixture()
			every { existOAuth2AccountPort.existBy(any(), any()) } returns false
			every { insertOAuth2AccountPort.insert(any()) } returns 5
			val command =
				LoadProviderAccountCommand(
					AuthProvider.GOOGLE,
					"token",
					"http://localhost:5173/oauth2/redirected/kakao",
				)
			val result = oAuth2SignUpService.signUp(command)

			result.accessToken.isNotBlank() shouldBe true
			result.refreshToken.isNotBlank() shouldBe true
		}

		"만약 기존에 존재하는 회원이면 예외를 반환한다" {
			every { accountLoader.getAccount(any()) } returns oAuth2ProviderAccountFixture()

			every { registerMemberUseCase.register(any()) } returns memberFixture()
			every { existOAuth2AccountPort.existBy(any(), any()) } returns true

			val command =
				LoadProviderAccountCommand(
					AuthProvider.GOOGLE,
					"token",
					"http://localhost:5173/oauth2/redirected/kakao",
				)

			shouldThrow<AlreadyExistAccountException> {
				oAuth2SignUpService.signUp(command)
			}
		}
	})