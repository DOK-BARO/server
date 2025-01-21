package kr.kro.dokbaro.server.core.account.application.port.input

/**
 * email 계정 업데이트 usecase 입니다.
 */
fun interface UpdateAccountEmailUseCase {
	fun updateEmail(
		memberId: Long,
		email: String,
	)
}