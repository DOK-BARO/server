package kr.kro.dokbaro.server.core.token.application.port.out

fun interface InsertRefreshTokenPort {
	fun insert(token: String)
}