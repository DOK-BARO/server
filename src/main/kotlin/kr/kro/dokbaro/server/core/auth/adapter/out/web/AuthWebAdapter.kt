package kr.kro.dokbaro.server.core.auth.adapter.out.web

import kr.kro.dokbaro.server.common.annotation.WebAdapter
import kr.kro.dokbaro.server.common.type.AuthProvider
import kr.kro.dokbaro.server.core.auth.application.port.out.LoadProviderAccountPort
import kr.kro.dokbaro.server.core.auth.application.port.out.LoadProviderResourceTokenPort
import kr.kro.dokbaro.server.core.auth.domain.ProviderAccount

@WebAdapter
class AuthWebAdapter(
	private val resourceTokenLoaders: Map<String, ProviderResourceTokenLoader>,
	private val accountLoaders: Map<String, ProviderAccountLoader>,
) : LoadProviderResourceTokenPort,
	LoadProviderAccountPort {
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
	): ProviderAccount =
		accountLoaders["${provider.name.lowercase()}AccountLoader"]!!
			.get(accessToken)
}