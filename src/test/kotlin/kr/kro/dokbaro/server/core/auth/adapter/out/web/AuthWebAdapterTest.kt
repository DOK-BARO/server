package kr.kro.dokbaro.server.core.auth.adapter.out.web

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.every
import io.mockk.mockk
import kr.kro.dokbaro.server.common.type.AuthProvider
import kr.kro.dokbaro.server.core.auth.adapter.out.web.github.GithubAccountLoader
import kr.kro.dokbaro.server.core.auth.adapter.out.web.github.GithubResourceTokenLoader
import kr.kro.dokbaro.server.core.auth.adapter.out.web.github.external.GithubAuthorizationClient
import kr.kro.dokbaro.server.core.auth.adapter.out.web.github.external.GithubAuthorizationTokenResponse
import kr.kro.dokbaro.server.core.auth.adapter.out.web.github.external.GithubResourceClient
import kr.kro.dokbaro.server.core.auth.adapter.out.web.github.external.resource.GithubAccount
import kr.kro.dokbaro.server.core.auth.adapter.out.web.google.GoogleAccountLoader
import kr.kro.dokbaro.server.core.auth.adapter.out.web.google.GoogleResourceTokenLoader
import kr.kro.dokbaro.server.core.auth.adapter.out.web.google.external.GoogleAuthorizationClient
import kr.kro.dokbaro.server.core.auth.adapter.out.web.google.external.GoogleAuthorizationTokenResponse
import kr.kro.dokbaro.server.core.auth.adapter.out.web.google.external.GoogleResourceClient
import kr.kro.dokbaro.server.core.auth.adapter.out.web.google.external.resource.GoogleAccount
import kr.kro.dokbaro.server.core.auth.adapter.out.web.kakao.KakaoAccountLoader
import kr.kro.dokbaro.server.core.auth.adapter.out.web.kakao.KakaoResourceTokenLoader
import kr.kro.dokbaro.server.core.auth.adapter.out.web.kakao.external.KakaoAuthorizationClient
import kr.kro.dokbaro.server.core.auth.adapter.out.web.kakao.external.KakaoAuthorizationTokenResponse
import kr.kro.dokbaro.server.core.auth.adapter.out.web.kakao.external.KakaoResourceClient
import kr.kro.dokbaro.server.core.auth.adapter.out.web.kakao.external.resource.KakaoAccount
import kr.kro.dokbaro.server.core.auth.adapter.out.web.naver.NaverAccountLoader
import kr.kro.dokbaro.server.core.auth.adapter.out.web.naver.NaverResourceTokenLoader
import kr.kro.dokbaro.server.core.auth.adapter.out.web.naver.external.NaverAuthorizationClient
import kr.kro.dokbaro.server.core.auth.adapter.out.web.naver.external.NaverAuthorizationTokenResponse
import kr.kro.dokbaro.server.core.auth.adapter.out.web.naver.external.NaverResourceClient
import kr.kro.dokbaro.server.core.auth.adapter.out.web.naver.external.resource.NaverAccount
import kr.kro.dokbaro.server.fixture.FixtureBuilder

class AuthWebAdapterTest :
	StringSpec({
		val githubAuthorizationClient = mockk<GithubAuthorizationClient>()
		val googleAuthorizationClient = mockk<GoogleAuthorizationClient>()
		val naverAuthorizationClient = mockk<NaverAuthorizationClient>()
		val kakaoAuthorizationClient = mockk<KakaoAuthorizationClient>()

		val githubResourceClient = mockk<GithubResourceClient>()
		val googleResourceClient = mockk<GoogleResourceClient>()
		val naverResourceClient = mockk<NaverResourceClient>()
		val kakaoResourceClient = mockk<KakaoResourceClient>()

		val resourceTokenLoaders: Map<String, ProviderResourceTokenLoader> =
			mapOf(
				"githubResourceTokenLoader" to GithubResourceTokenLoader(githubAuthorizationClient, "grant", "cid"),
				"googleResourceTokenLoader" to
					GoogleResourceTokenLoader(
						googleAuthorizationClient,
						"grant",
						"cid",
						"csecret",
					),
				"naverResourceTokenLoader" to
					NaverResourceTokenLoader(
						naverAuthorizationClient,
						"grant",
						"cid",
						"csecret",
					),
				"kakaoResourceTokenLoader" to
					KakaoResourceTokenLoader(
						kakaoAuthorizationClient,
						"grant",
						"cid",
						"csecret",
					),
			)

		val accountLoaders: Map<String, ProviderAccountLoader> =
			mapOf(
				"githubAccountLoader" to GithubAccountLoader(githubResourceClient),
				"googleAccountLoader" to GoogleAccountLoader(googleResourceClient),
				"naverAccountLoader" to NaverAccountLoader(naverResourceClient),
				"kakaoAccountLoader" to KakaoAccountLoader(kakaoResourceClient),
			)

		val adapter = AuthWebAdapter(resourceTokenLoaders, accountLoaders)

		"acccess token을 발급받는다" {
			every { githubAuthorizationClient.getAuthorizationToken(any(), any(), any(), any()) } returns
				GithubAuthorizationTokenResponse("token", "scope", "type")
			every { googleAuthorizationClient.getAuthorizationToken(any(), any(), any(), any(), any()) } returns
				GoogleAuthorizationTokenResponse("bearer", "access", 60000, "ref", "scope")
			every { naverAuthorizationClient.getAuthorizationToken(any(), any(), any(), any(), any()) } returns
				NaverAuthorizationTokenResponse("token", "scope", "type", 60000)
			every { kakaoAuthorizationClient.getAuthorizationToken(any(), any(), any(), any(), any()) } returns
				KakaoAuthorizationTokenResponse("token", "access", "id", 60000, "ref", 40, "scope")

			AuthProvider.entries.forEach {
				val token = adapter.getToken(it, "token", "redirectUrl")
				token.isNotEmpty() shouldBe true
			}
		}

		"provider account를 반환한다" {

			AuthProvider.entries.forEach {
				every { githubResourceClient.getUserProfiles(any()) } returns
					FixtureBuilder
						.give<GithubAccount>()
						.sample()
				every { googleResourceClient.getUserProfiles(any()) } returns
					FixtureBuilder
						.give<GoogleAccount>()
						.sample()
				every { naverResourceClient.getUserProfiles(any()) } returns
					FixtureBuilder
						.give<NaverAccount>()
						.sample()
				every { kakaoResourceClient.getUserProfiles(any()) } returns
					FixtureBuilder
						.give<KakaoAccount>()
						.sample()

				val account = adapter.getProviderAccount(it, "token")

				account shouldNotBe null
				account.id shouldNotBe null
			}
		}
	})