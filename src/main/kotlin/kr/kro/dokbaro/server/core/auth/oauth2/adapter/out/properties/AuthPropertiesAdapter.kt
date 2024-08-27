package kr.kro.dokbaro.server.core.auth.oauth2.adapter.out.properties

import kr.kro.dokbaro.server.common.annotation.PropertiesAdapter
import kr.kro.dokbaro.server.common.type.AuthProvider
import kr.kro.dokbaro.server.core.auth.oauth2.application.port.out.provider.LoadOAuth2ProviderAuthorizationServerUrlPort

@PropertiesAdapter
class AuthPropertiesAdapter(
	private val urlLoaders: Map<String, OAuth2AuthorizeServerUrlLoader>,
) : LoadOAuth2ProviderAuthorizationServerUrlPort {
	override fun getUrl(
		provider: AuthProvider,
		redirectUrl: String,
	): String = urlLoaders["${provider.name.lowercase()}AuthorizeServerUrlLoader"]!!.getUrl(redirectUrl)
}