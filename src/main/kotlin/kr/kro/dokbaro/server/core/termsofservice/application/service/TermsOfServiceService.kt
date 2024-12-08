package kr.kro.dokbaro.server.core.termsofservice.application.service

import kr.kro.dokbaro.server.core.termsofservice.application.port.input.AgreeTermsOfServiceUseCase
import kr.kro.dokbaro.server.core.termsofservice.application.port.input.FindAllTermsOfServiceUseCase
import kr.kro.dokbaro.server.core.termsofservice.application.port.input.FindTermsOfServiceDetailUseCase
import kr.kro.dokbaro.server.core.termsofservice.application.port.out.InsertAgreeTermsOfServicePersistencePort
import kr.kro.dokbaro.server.core.termsofservice.application.port.out.LoadTermsOfServiceDetailPort
import kr.kro.dokbaro.server.core.termsofservice.domain.AgreeTermsOfService
import kr.kro.dokbaro.server.core.termsofservice.domain.TermsOfService
import kr.kro.dokbaro.server.core.termsofservice.query.TermsOfServiceDetail
import kr.kro.dokbaro.server.core.termsofservice.query.TermsOfServiceSummary
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class TermsOfServiceService(
	private val loadTermsOfServiceDetailPort: LoadTermsOfServiceDetailPort,
	private val insertAgreeTermsOfServicePersistencePort: InsertAgreeTermsOfServicePersistencePort,
) : FindAllTermsOfServiceUseCase,
	FindTermsOfServiceDetailUseCase,
	AgreeTermsOfServiceUseCase {
	override fun findAll(): Collection<TermsOfServiceSummary> =
		TermsOfService.entries.map {
			TermsOfServiceSummary(
				id = it.id,
				title = it.title,
				subTitle = it.subTitle,
				hasDetail = it.hasDetail,
				primary = it.primary,
			)
		}

	override fun findDetail(id: Long): TermsOfServiceDetail =
		loadTermsOfServiceDetailPort.getDetail(id) ?: throw NotFoundTermsOfServiceException(id)

	override fun agree(
		authId: UUID,
		items: Collection<Long>,
	) {
		insertAgreeTermsOfServicePersistencePort.insertAgree(
			AgreeTermsOfService(
				TODO(),
				items.map { itemId ->
					TermsOfService.entries.find { t -> t.id == itemId }
						?: throw NotFoundTermsOfServiceException(itemId)
				},
			),
		)
	}
}