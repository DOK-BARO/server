package kr.kro.dokbaro.server.core.auth.oauth2.application.service

import com.navercorp.fixturemonkey.kotlin.setExp
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import kr.kro.dokbaro.server.common.type.AuthProvider
import kr.kro.dokbaro.server.core.auth.oauth2.application.port.dto.LoadProviderAccountCommand
import kr.kro.dokbaro.server.core.auth.oauth2.application.port.dto.OAuth2ProviderAccount
import kr.kro.dokbaro.server.core.auth.oauth2.application.port.out.ExistOAuth2AccountPort
import kr.kro.dokbaro.server.core.auth.oauth2.application.port.out.SaveOAuth2AccountPort
import kr.kro.dokbaro.server.core.auth.oauth2.application.service.exception.AlreadyExistAccountException
import kr.kro.dokbaro.server.core.member.application.port.input.command.RegisterMemberUseCase
import kr.kro.dokbaro.server.core.member.domain.Member
import kr.kro.dokbaro.server.core.token.application.port.input.GenerateAuthTokenUseCase
import kr.kro.dokbaro.server.core.token.domain.AuthToken
import kr.kro.dokbaro.server.fixture.FixtureBuilder

class OAuth2SignUpServiceTest :
	StringSpec({
		val accountLoader = mockk<OAuth2ProviderAccountLoader>()
		val registerMemberUseCase = mockk<RegisterMemberUseCase>()
		val existOAuth2AccountPort = mockk<ExistOAuth2AccountPort>()
		val saveOAuth2AccountPort = mockk<SaveOAuth2AccountPort>()
		val generateAuthTokenUseCase = mockk<GenerateAuthTokenUseCase>()

		val oAuth2SignUpService =
			OAuth2SignUpService(
				accountLoader,
				registerMemberUseCase,
				existOAuth2AccountPort,
				saveOAuth2AccountPort,
				generateAuthTokenUseCase,
			)

		"회원가입을 수행한다" {
			every { accountLoader.getAccount(any()) } returns
				FixtureBuilder
					.give<OAuth2ProviderAccount>()
					.setExp(OAuth2ProviderAccount::provider, AuthProvider.KAKAO)
					.setExp(OAuth2ProviderAccount::id, "accountId")
					.setExp(OAuth2ProviderAccount::name, "name")
					.setExp(OAuth2ProviderAccount::email, "aaa@example.com")
					.setExp(OAuth2ProviderAccount::profileImage, "profile.png")
					.sample()

			every { generateAuthTokenUseCase.generate(any()) } returns
				FixtureBuilder
					.give<AuthToken>()
					.setExp(AuthToken::accessToken, "accessToken")
					.setExp(AuthToken::refreshToken, "refreshToken")
					.sample()

			every { registerMemberUseCase.register(any()) } returns
				FixtureBuilder
					.give<Member>()
					.sample()
			every { existOAuth2AccountPort.existBy(any(), any()) } returns false
			every { saveOAuth2AccountPort.save(any()) } returns 5
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
			every { accountLoader.getAccount(any()) } returns
				FixtureBuilder
					.give<OAuth2ProviderAccount>()
					.setExp(OAuth2ProviderAccount::provider, AuthProvider.KAKAO)
					.setExp(OAuth2ProviderAccount::id, "accountId")
					.setExp(OAuth2ProviderAccount::name, "name")
					.setExp(OAuth2ProviderAccount::email, "aaa@example.com")
					.setExp(OAuth2ProviderAccount::profileImage, "profile.png")
					.sample()

			every { registerMemberUseCase.register(any()) } returns
				FixtureBuilder
					.give<Member>()
					.sample()
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