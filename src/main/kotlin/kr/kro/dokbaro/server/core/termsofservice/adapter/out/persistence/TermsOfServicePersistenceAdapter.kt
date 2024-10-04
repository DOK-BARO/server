package kr.kro.dokbaro.server.core.termsofservice.adapter.out.persistence

import kr.kro.dokbaro.server.common.annotation.PersistenceAdapter
import kr.kro.dokbaro.server.core.termsofservice.adapter.out.persistence.repository.jooq.TermsOfServiceRepository
import kr.kro.dokbaro.server.core.termsofservice.application.port.out.InsertAgreeTermsOfServicePersistencePort
import kr.kro.dokbaro.server.core.termsofservice.application.port.out.LoadTermsOfServiceDetailPort
import kr.kro.dokbaro.server.core.termsofservice.domain.AgreeTermsOfService
import kr.kro.dokbaro.server.core.termsofservice.query.TermsOfServiceDetail

@PersistenceAdapter
class TermsOfServicePersistenceAdapter(
	private val termsOfServiceRepository: TermsOfServiceRepository,
) : InsertAgreeTermsOfServicePersistencePort,
	LoadTermsOfServiceDetailPort {
	override fun insertAgree(agreeTermsOfService: AgreeTermsOfService) {
		termsOfServiceRepository.insertAgreeTermsOfService(agreeTermsOfService)
	}

	override fun getDetail(id: Long): TermsOfServiceDetail? = termsOfServiceRepository.getDetail(id)
}