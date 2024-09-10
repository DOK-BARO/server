package kr.kro.dokbaro.server.core.emailauthentication.adapter.out.persistence

import kr.kro.dokbaro.server.common.annotation.PersistenceAdapter
import kr.kro.dokbaro.server.core.emailauthentication.adapter.out.persistence.repository.jooq.EmailAuthenticationQueryRepository
import kr.kro.dokbaro.server.core.emailauthentication.adapter.out.persistence.repository.jooq.EmailAuthenticationRepository
import kr.kro.dokbaro.server.core.emailauthentication.application.port.out.ExistEmailAuthenticationPort
import kr.kro.dokbaro.server.core.emailauthentication.application.port.out.FindEmailAuthenticationPort
import kr.kro.dokbaro.server.core.emailauthentication.application.port.out.SaveEmailAuthenticationPort
import kr.kro.dokbaro.server.core.emailauthentication.application.port.out.UpdateEmailAuthenticationPort
import kr.kro.dokbaro.server.core.emailauthentication.application.port.out.dto.SearchEmailAuthenticationCondition
import kr.kro.dokbaro.server.core.emailauthentication.domain.EmailAuthentication

@PersistenceAdapter
class EmailAuthenticationPersistenceAdapter(
	private val emailAuthenticationRepository: EmailAuthenticationRepository,
	private val emailAuthenticationQueryRepository: EmailAuthenticationQueryRepository,
) : ExistEmailAuthenticationPort,
	FindEmailAuthenticationPort,
	SaveEmailAuthenticationPort,
	UpdateEmailAuthenticationPort {
	override fun existBy(condition: SearchEmailAuthenticationCondition): Boolean =
		emailAuthenticationQueryRepository.existBy(condition)

	override fun findBy(condition: SearchEmailAuthenticationCondition): EmailAuthentication? =
		emailAuthenticationQueryRepository.findBy(condition)

	override fun save(emailAuthentication: EmailAuthentication): Long =
		emailAuthenticationRepository.save(emailAuthentication)

	override fun update(emailAuthentication: EmailAuthentication) {
		emailAuthenticationRepository.update(emailAuthentication)
	}
}