package kr.kro.dokbaro.server.domain.account.port.output

interface ExistAccountPort {
	fun existBy(socialId: String): Boolean

	fun notExistBy(socialId: String): Boolean = !existBy(socialId)
}