package kr.kro.dokbaro.server.domain.token.port.output

fun interface SaveRefreshTokenPort {
	fun save(token: String)
}