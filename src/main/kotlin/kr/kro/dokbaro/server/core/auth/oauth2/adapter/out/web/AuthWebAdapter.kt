package kr.kro.dokbaro.server.core.auth.oauth2.adapter.out.web

import kr.kro.dokbaro.server.common.annotation.WebAdapter
import kr.kro.dokbaro.server.common.type.AuthProvider
import kr.kro.dokbaro.server.core.auth.oauth2.application.port.dto.OAuth2ProviderAccount
import kr.kro.dokbaro.server.core.auth.oauth2.application.port.out.provider.LoadOAuth2ProviderAccountPort
import kr.kro.dokbaro.server.core.auth.oauth2.application.port.out.provider.LoadOAuth2ProviderResourceTokenPort

@WebAdapter
class AuthWebAdapter(
	private val resourceTokenLoaders: Map<String, ProviderResourceTokenLoader>,
	private val accountLoaders: Map<String, ProviderAccountLoader>,
) : LoadOAuth2ProviderResourceTokenPort,
	LoadOAuth2ProviderAccountPort {
	override fun getToken(
		provider: AuthProvider,
		authorizationToken: String,
		redirectUrl: String,
	): String =
		resourceTokenLoaders["${provider.name.lowercase()}ResourceTokenLoader"]!!
			.get(authorizationToken, redirectUrl)

	override fun getProviderAccount(
		provider: AuthProvider,
		accessToken: String,
	): OAuth2ProviderAccount =
		accountLoaders["${provider.name.lowercase()}AccountLoader"]!!
			.get(accessToken)
}