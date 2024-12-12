package kr.kro.dokbaro.server.core.account.application.service

import kr.kro.dokbaro.server.core.account.application.port.out.UpdateAccountPasswordPort
import kr.kro.dokbaro.server.core.account.domain.AccountPassword

class UpdateAccountPasswordPortMock : UpdateAccountPasswordPort {
	val storage = mutableListOf<AccountPassword>()

	override fun updateAccountPassword(accountPassword: AccountPassword) {
		storage.add(accountPassword)
	}

	fun clear() {
		storage.clear()
	}
}