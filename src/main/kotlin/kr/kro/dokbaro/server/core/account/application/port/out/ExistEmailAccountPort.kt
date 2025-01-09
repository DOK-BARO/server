package kr.kro.dokbaro.server.core.account.application.port.out

fun interface ExistEmailAccountPort {
	fun existsByEmail(email: String): Boolean
}