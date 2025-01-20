package kr.kro.dokbaro.server.core.account.application.port.input

import kr.kro.dokbaro.server.core.account.application.port.input.dto.ChangePasswordCommand

/**
 * password 변경 usecase 입니다.
 */
fun interface ChangePasswordUseCase {
	fun changePassword(command: ChangePasswordCommand)
}