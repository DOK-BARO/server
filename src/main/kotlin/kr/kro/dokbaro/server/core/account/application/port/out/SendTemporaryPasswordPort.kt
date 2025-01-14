package kr.kro.dokbaro.server.core.account.application.port.out

/**
 * 임시 비밀번호를 전송하는 port 입니다.
 */
fun interface SendTemporaryPasswordPort {
	fun sendTemporaryPassword(
		email: String,
		password: String,
	)
}