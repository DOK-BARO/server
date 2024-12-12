package kr.kro.dokbaro.server.core.account.application.port.input

fun interface IssueTemporaryPasswordUseCase {
	fun issueTemporaryPassword(email: String)
}