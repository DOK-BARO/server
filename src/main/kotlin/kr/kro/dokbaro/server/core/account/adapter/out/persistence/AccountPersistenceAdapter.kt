package kr.kro.dokbaro.server.core.account.adapter.out.persistence

import kr.kro.dokbaro.server.common.annotation.PersistenceAdapter
import kr.kro.dokbaro.server.common.type.AuthProvider
import kr.kro.dokbaro.server.core.account.adapter.out.persistence.repository.jooq.AccountQueryRepository
import kr.kro.dokbaro.server.core.account.adapter.out.persistence.repository.jooq.AccountRepository
import kr.kro.dokbaro.server.core.account.application.port.out.ExistAccountPort
import kr.kro.dokbaro.server.core.account.application.port.out.LoadAccountPort
import kr.kro.dokbaro.server.core.account.application.port.out.SaveAccountPort
import kr.kro.dokbaro.server.core.account.domain.Account

@PersistenceAdapter
class AccountPersistenceAdapter(
	private val repository: AccountRepository,
	private val queryRepository: AccountQueryRepository,
) : ExistAccountPort,
	SaveAccountPort,
	LoadAccountPort {
	override fun existBy(
		socialId: String,
		provider: AuthProvider,
	): Boolean = queryRepository.existBy(socialId, provider)

	override fun save(account: Account): Long = repository.save(account)

	override fun findBy(socialId: String): Account? = queryRepository.findBy(socialId)
}