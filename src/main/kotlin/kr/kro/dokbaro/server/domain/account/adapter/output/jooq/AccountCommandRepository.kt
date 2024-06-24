package kr.kro.dokbaro.server.domain.account.adapter.output.jooq

import kr.kro.dokbaro.server.domain.account.model.Account
import kr.kro.dokbaro.server.domain.account.port.output.ExistAccountPort
import kr.kro.dokbaro.server.domain.account.port.output.SaveAccountPort
import org.jooq.DSLContext

class AccountCommandRepository(
	private val dslContext: DSLContext,
) : ExistAccountPort,
	SaveAccountPort {
	override fun existBy(socialId: String): Boolean {
		TODO("Not yet implemented")
	}

	override fun save(account: Account) {
		TODO("Not yet implemented")
	}
}