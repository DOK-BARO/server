package kr.kro.dokbaro.server.core.auth.email.adapter.out.persistence

import kr.kro.dokbaro.server.common.annotation.PersistenceAdapter
import kr.kro.dokbaro.server.core.auth.email.adapter.out.persistence.repository.jooq.AccountPasswordRepository
import kr.kro.dokbaro.server.core.auth.email.adapter.out.persistence.repository.jooq.EmailCertificatedAccountQueryRepository
import kr.kro.dokbaro.server.core.auth.email.application.port.out.InsertEmailAccountPort
import kr.kro.dokbaro.server.core.auth.email.application.port.out.LoadEmailCertificatedAccountPort
import kr.kro.dokbaro.server.core.auth.email.domain.AccountPassword
import kr.kro.dokbaro.server.core.auth.email.domain.EmailCertificatedAccount

@PersistenceAdapter
class EmailAccountPersistenceAdapter(
	private val accountPasswordRepository: AccountPasswordRepository,
	private val emailCertificatedAccountQueryRepository: EmailCertificatedAccountQueryRepository,
) : LoadEmailCertificatedAccountPort,
	InsertEmailAccountPort {
	override fun findByEmail(email: String): EmailCertificatedAccount? =
		emailCertificatedAccountQueryRepository.findByEmail(email)

	override fun insert(accountPassword: AccountPassword): Long = accountPasswordRepository.insert(accountPassword)
}