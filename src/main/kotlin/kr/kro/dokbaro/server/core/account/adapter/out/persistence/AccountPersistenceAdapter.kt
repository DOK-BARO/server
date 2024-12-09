package kr.kro.dokbaro.server.core.account.adapter.out.persistence

import kr.kro.dokbaro.server.common.annotation.PersistenceAdapter
import kr.kro.dokbaro.server.core.account.adapter.out.persistence.repository.jooq.AccountRepository
import kr.kro.dokbaro.server.core.account.application.port.out.InsertAccountPasswordPort
import kr.kro.dokbaro.server.core.account.application.port.out.InsertSocialAccountPort
import kr.kro.dokbaro.server.core.account.domain.AccountPassword
import kr.kro.dokbaro.server.core.account.domain.SocialAccount

@PersistenceAdapter
class AccountPersistenceAdapter(
	private val accountRepository: AccountRepository,
) : InsertAccountPasswordPort,
	InsertSocialAccountPort {
	override fun insertSocialAccount(socialAccount: SocialAccount) {
		accountRepository.insertSocialAccount(socialAccount)
	}

	override fun insertAccountPassword(accountPassword: AccountPassword) {
		accountRepository.insertAccountPassword(accountPassword)
	}
}