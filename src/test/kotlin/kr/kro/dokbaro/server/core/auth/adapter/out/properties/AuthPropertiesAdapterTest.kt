package kr.kro.dokbaro.server.core.auth.adapter.out.properties

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import kr.kro.dokbaro.server.common.type.AuthProvider
import kr.kro.dokbaro.server.core.auth.adapter.out.properties.provider.GithubAuthorizeServerUrlLoader
import kr.kro.dokbaro.server.core.auth.adapter.out.properties.provider.GoogleAuthorizeServerUrlLoader
import kr.kro.dokbaro.server.core.auth.adapter.out.properties.provider.KakaoAuthorizeServerUrlLoader
import kr.kro.dokbaro.server.core.auth.adapter.out.properties.provider.NaverAuthorizeServerUrlLoader

class AuthPropertiesAdapterTest :
	StringSpec({
		val urlLoaders: Map<String, OAuth2AuthorizeServerUrlLoader> =
			mapOf(
				"githubAuthorizeServerUrlLoader" to
					GithubAuthorizeServerUrlLoader(
						"authorizationUrl",
						"path",
						"clientUrl",
						"id",
					),
				"googleAuthorizeServerUrlLoader" to
					GoogleAuthorizeServerUrlLoader(
						"authorizationUrl",
						"path",
						"clientUrl",
						"id",
					),
				"naverAuthorizeServerUrlLoader" to
					NaverAuthorizeServerUrlLoader(
						"authorizationUrl",
						"path",
						"clientUrl",
						"id",
					),
				"kakaoAuthorizeServerUrlLoader" to
					KakaoAuthorizeServerUrlLoader(
						"authorizationUrl",
						"path",
						"clientUrl",
						"id",
						"scope",
					),
			)

		val adapter = AuthPropertiesAdapter(urlLoaders)

		"url 반환을 수행한다" {
			AuthProvider.entries.forEach {
				adapter.getUrl(it).isNotEmpty() shouldBe true
			}
		}
	})