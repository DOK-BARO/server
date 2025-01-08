package kr.kro.dokbaro.server.core.account.adapter.out.persistence

import kr.kro.dokbaro.server.common.annotation.PersistenceAdapter
import kr.kro.dokbaro.server.core.account.adapter.out.persistence.repository.jooq.AccountQueryRepository
import kr.kro.dokbaro.server.core.account.application.port.out.ExistEmailAccountPort
import kr.kro.dokbaro.server.core.account.application.port.out.LoadEmailAccountPort
import kr.kro.dokbaro.server.core.account.domain.EmailAccount

@PersistenceAdapter
class EmailAccountPersistenceQueryAdapter(
	private val accountQueryRepository: AccountQueryRepository,
) : LoadEmailAccountPort,
	ExistEmailAccountPort {
	override fun findByMemberId(memberId: Long): EmailAccount? = accountQueryRepository.findByMemberId(memberId)

	override fun findByEmail(email: String): EmailAccount? = accountQueryRepository.findByEmail(email)

	override fun existsByEmail(email: String): Boolean = accountQueryRepository.existsByEmail(email)
}