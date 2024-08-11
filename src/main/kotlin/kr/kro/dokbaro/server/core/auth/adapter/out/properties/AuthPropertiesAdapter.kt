package kr.kro.dokbaro.server.core.auth.adapter.out.properties

import kr.kro.dokbaro.server.common.annotation.PropertiesAdapter
import kr.kro.dokbaro.server.common.type.AuthProvider
import kr.kro.dokbaro.server.core.auth.application.port.out.LoadProviderAuthorizationServerUrlPort

@PropertiesAdapter
class AuthPropertiesAdapter(
	private val urlLoaders: Map<String, OAuth2AuthorizeServerUrlLoader>,
) : LoadProviderAuthorizationServerUrlPort {
	override fun getUrl(
		provider: AuthProvider,
		redirectUrl: String,
	): String = urlLoaders["${provider.name.lowercase()}AuthorizeServerUrlLoader"]!!.getUrl(redirectUrl)
}