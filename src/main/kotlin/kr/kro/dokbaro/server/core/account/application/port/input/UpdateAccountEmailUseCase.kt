package kr.kro.dokbaro.server.core.account.application.port.input

fun interface UpdateAccountEmailUseCase {
	fun updateEmail(
		memberId: Long,
		email: String,
	)
}