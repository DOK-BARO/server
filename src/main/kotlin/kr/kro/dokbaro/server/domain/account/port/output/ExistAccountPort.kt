package kr.kro.dokbaro.server.domain.account.port.output

import kr.kro.dokbaro.server.domain.account.model.Provider

interface ExistAccountPort {
	fun existBy(
		socialId: String,
		provider: Provider,
	): Boolean

	fun notExistBy(
		socialId: String,
		provider: Provider,
	): Boolean = !existBy(socialId, provider)
}