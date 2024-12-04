package kr.kro.dokbaro.server.security.oauth2

interface OAuthUser {
	val id: String
	val email: String
	val nickname: String
	val provider: AuthProvider
	val profileImage: String?
}