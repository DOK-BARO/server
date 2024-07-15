package kr.kro.dokbaro.server.domain.account.port.output

import kr.kro.dokbaro.server.global.AuthProvider

interface ExistAccountPort {
	fun existBy(
		socialId: String,
		provider: AuthProvider,
	): Boolean

	fun notExistBy(
		socialId: String,
		provider: AuthProvider,
	): Boolean = !existBy(socialId, provider)
}