package kr.kro.dokbaro.server.core.account.application.port.input

import kr.kro.dokbaro.server.core.account.application.port.input.dto.RegisterEmailAccountCommand
import java.util.UUID

fun interface RegisterEmailAccountUseCase {
	fun registerEmailAccount(command: RegisterEmailAccountCommand): UUID
}