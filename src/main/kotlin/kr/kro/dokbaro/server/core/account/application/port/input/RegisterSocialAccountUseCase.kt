package kr.kro.dokbaro.server.core.account.application.port.input

import kr.kro.dokbaro.server.core.account.application.port.input.dto.RegisterSocialAccountCommand
import java.util.UUID

/**
 * social 계정 등록 usecase 입니다.
 */
fun interface RegisterSocialAccountUseCase {
	/**
	 * @return UUID(member certification ID)
	 */
	fun registerSocialAccount(command: RegisterSocialAccountCommand): UUID
}