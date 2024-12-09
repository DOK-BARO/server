package kr.kro.dokbaro.server.core.account.application.port.input

import kr.kro.dokbaro.server.core.account.application.port.input.dto.RegisterSocialAccountCommand
import java.util.UUID

fun interface RegisterSocialAccountUseCase {
	fun registerSocialAccount(command: RegisterSocialAccountCommand): UUID
}