package kr.kro.dokbaro.server.core.account.application.service

import kr.kro.dokbaro.server.core.account.application.port.input.UpdateAccountEmailUseCase
import kr.kro.dokbaro.server.core.account.application.port.out.LoadEmailAccountPort
import kr.kro.dokbaro.server.core.account.application.port.out.UpdateEmailAccountPort
import kr.kro.dokbaro.server.core.emailauthentication.application.port.input.UseAuthenticatedEmailUseCase
import org.springframework.stereotype.Service

@Service
class UpdateEmailService(
	private val loadEmailAccountPort: LoadEmailAccountPort,
	private val updateEmailAccountPort: UpdateEmailAccountPort,
	private val useAuthenticatedEmailUseCase: UseAuthenticatedEmailUseCase,
) : UpdateAccountEmailUseCase {
	override fun updateEmail(
		memberId: Long,
		email: String,
	) {
		useAuthenticatedEmailUseCase.useEmail(email = email)
		loadEmailAccountPort.findByMemberId(memberId)?.let { account ->
			account.changeEmail(email)
			updateEmailAccountPort.updateEmailAccount(account)
		}
	}
}