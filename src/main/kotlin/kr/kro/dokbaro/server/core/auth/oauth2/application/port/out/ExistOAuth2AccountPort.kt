package kr.kro.dokbaro.server.core.auth.oauth2.application.port.out

import kr.kro.dokbaro.server.common.type.AuthProvider

interface ExistOAuth2AccountPort {
	fun existBy(
		socialId: String,
		provider: AuthProvider,
	): Boolean

	fun notExistBy(
		socialId: String,
		provider: AuthProvider,
	): Boolean = !existBy(socialId, provider)
}