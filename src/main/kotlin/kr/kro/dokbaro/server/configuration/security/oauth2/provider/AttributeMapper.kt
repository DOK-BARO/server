package kr.kro.dokbaro.server.configuration.security.oauth2.provider

interface AttributeMapper {
	fun map(attributes: Map<String, Any>): OAuthProviderResponse
}