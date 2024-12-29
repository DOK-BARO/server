package kr.kro.dokbaro.server.core.account.adapter.out.persistence

import kr.kro.dokbaro.server.common.annotation.PersistenceAdapter
import kr.kro.dokbaro.server.core.account.adapter.out.persistence.repository.jooq.AccountQueryRepository
import kr.kro.dokbaro.server.core.account.application.port.out.LoadAccountPasswordPort
import kr.kro.dokbaro.server.core.account.domain.EmailAccount

@PersistenceAdapter
class AccountPersistenceQueryAdapter(
	private val accountQueryRepository: AccountQueryRepository,
) : LoadAccountPasswordPort {
	override fun findByMemberId(memberId: Long): EmailAccount? = accountQueryRepository.findByMemberId(memberId)

	override fun findByEmail(email: String): EmailAccount? = accountQueryRepository.findByEmail(email)
}