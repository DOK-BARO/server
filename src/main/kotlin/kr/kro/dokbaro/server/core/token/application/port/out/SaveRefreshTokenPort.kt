package kr.kro.dokbaro.server.core.token.application.port.out

fun interface SaveRefreshTokenPort {
	fun save(token: String)
}