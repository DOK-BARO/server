package kr.kro.dokbaro.server.core.account.application.port.input

import kr.kro.dokbaro.server.core.account.application.port.input.dto.ChangePasswordCommand

fun interface ChangePasswordUseCase {
	fun changePassword(command: ChangePasswordCommand)
}