package kr.kro.dokbaro.server.core.account.application.port.input

/**
 * 임시 비밀번호 발급 usecase 입니다.
 */
fun interface IssueTemporaryPasswordUseCase {
	fun issueTemporaryPassword(email: String)
}