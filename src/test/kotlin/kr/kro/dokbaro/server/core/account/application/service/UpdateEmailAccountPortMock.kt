package kr.kro.dokbaro.server.core.account.application.service

import kr.kro.dokbaro.server.core.account.application.port.out.UpdateEmailAccountPort
import kr.kro.dokbaro.server.core.account.domain.EmailAccount

class UpdateEmailAccountPortMock : UpdateEmailAccountPort {
	val storage = mutableListOf<EmailAccount>()

	override fun updateEmailAccount(emailAccount: EmailAccount) {
		storage.add(emailAccount)
	}

	fun clear() {
		storage.clear()
	}
}