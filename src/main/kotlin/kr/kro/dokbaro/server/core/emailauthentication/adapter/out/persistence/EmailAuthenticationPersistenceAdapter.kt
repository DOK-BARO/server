package kr.kro.dokbaro.server.core.emailauthentication.adapter.out.persistence

import kr.kro.dokbaro.server.common.annotation.PersistenceAdapter
import kr.kro.dokbaro.server.core.emailauthentication.adapter.out.persistence.repository.jooq.EmailAuthenticationJooqQueryRepository
import kr.kro.dokbaro.server.core.emailauthentication.adapter.out.persistence.repository.jooq.EmailAuthenticationJooqRepository
import kr.kro.dokbaro.server.core.emailauthentication.application.port.out.ExistEmailAuthenticationPort
import kr.kro.dokbaro.server.core.emailauthentication.application.port.out.FindEmailAuthenticationPort
import kr.kro.dokbaro.server.core.emailauthentication.application.port.out.SaveEmailAuthenticationPort
import kr.kro.dokbaro.server.core.emailauthentication.application.port.out.UpdateEmailAuthenticationPort
import kr.kro.dokbaro.server.core.emailauthentication.application.port.out.dto.SearchEmailAuthenticationCondition
import kr.kro.dokbaro.server.core.emailauthentication.domain.EmailAuthentication

@PersistenceAdapter
class EmailAuthenticationPersistenceAdapter(
	private val emailAuthenticationJooqRepository: EmailAuthenticationJooqRepository,
	private val emailAuthenticationJooqQueryRepository: EmailAuthenticationJooqQueryRepository,
) : ExistEmailAuthenticationPort,
	FindEmailAuthenticationPort,
	SaveEmailAuthenticationPort,
	UpdateEmailAuthenticationPort {
	override fun existBy(condition: SearchEmailAuthenticationCondition): Boolean =
		emailAuthenticationJooqQueryRepository.existBy(condition)

	override fun findBy(condition: SearchEmailAuthenticationCondition): EmailAuthentication? =
		emailAuthenticationJooqQueryRepository.findBy(condition)

	override fun save(emailAuthentication: EmailAuthentication): Long =
		emailAuthenticationJooqRepository.save(emailAuthentication)

	override fun update(emailAuthentication: EmailAuthentication) {
		emailAuthenticationJooqRepository.update(emailAuthentication)
	}
}