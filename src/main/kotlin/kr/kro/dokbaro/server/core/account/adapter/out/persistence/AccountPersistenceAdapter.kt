package kr.kro.dokbaro.server.core.account.adapter.out.persistence

import kr.kro.dokbaro.server.common.annotation.PersistenceAdapter
import kr.kro.dokbaro.server.core.account.adapter.out.persistence.repository.jooq.AccountRepository
import kr.kro.dokbaro.server.core.account.application.port.out.InsertAccountPasswordPort
import kr.kro.dokbaro.server.core.account.application.port.out.InsertSocialAccountPort
import kr.kro.dokbaro.server.core.account.application.port.out.UpdateEmailAccountPort
import kr.kro.dokbaro.server.core.account.domain.EmailAccount
import kr.kro.dokbaro.server.core.account.domain.SocialAccount

@PersistenceAdapter
class AccountPersistenceAdapter(
	private val accountRepository: AccountRepository,
) : InsertAccountPasswordPort,
	InsertSocialAccountPort,
	UpdateEmailAccountPort {
	override fun insertSocialAccount(socialAccount: SocialAccount) {
		accountRepository.insertSocialAccount(socialAccount)
	}

	override fun insertEmailAccount(emailAccount: EmailAccount) {
		accountRepository.insertEmailAccount(emailAccount)
	}

	override fun updateEmailAccount(emailAccount: EmailAccount) {
		accountRepository.updateEmailAccount(emailAccount)
	}
}