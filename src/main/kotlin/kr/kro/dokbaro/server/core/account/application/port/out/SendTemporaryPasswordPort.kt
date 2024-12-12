package kr.kro.dokbaro.server.core.account.application.port.out

fun interface SendTemporaryPasswordPort {
	fun sendTemporaryPassword(
		email: String,
		password: String,
	)
}