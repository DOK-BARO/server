package kr.kro.dokbaro.server.core.account.application.port.input

import kr.kro.dokbaro.server.core.account.application.port.input.dto.RegisterEmailAccountCommand
import java.util.UUID

/**
 * email 계정 등록 usecase 입니다.
 */
fun interface RegisterEmailAccountUseCase {
	fun registerEmailAccount(command: RegisterEmailAccountCommand): UUID
}