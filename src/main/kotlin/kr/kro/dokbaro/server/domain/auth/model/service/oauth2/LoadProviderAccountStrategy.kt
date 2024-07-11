package kr.kro.dokbaro.server.domain.auth.model.service.oauth2

interface LoadProviderAccountStrategy {
	fun load(authorizationToken: String): ProviderAccount
}