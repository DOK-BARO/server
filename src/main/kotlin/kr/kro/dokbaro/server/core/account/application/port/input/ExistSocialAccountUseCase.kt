package kr.kro.dokbaro.server.core.account.application.port.input

import kr.kro.dokbaro.server.core.account.application.port.input.dto.ExistSocialAccountCommand

fun interface ExistSocialAccountUseCase {
	fun existsBy(command: ExistSocialAccountCommand): Boolean
}