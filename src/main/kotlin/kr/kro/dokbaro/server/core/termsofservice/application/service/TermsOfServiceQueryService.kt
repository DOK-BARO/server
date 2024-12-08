package kr.kro.dokbaro.server.core.termsofservice.application.service

import kr.kro.dokbaro.server.core.termsofservice.application.port.input.FindAgreeAllRequiredTermsOfServiceUseCase
import kr.kro.dokbaro.server.core.termsofservice.application.port.out.ReadMemberAgreeTermsOfServicePort
import kr.kro.dokbaro.server.core.termsofservice.application.port.out.dto.MemberAgreeTermsOfServiceElement
import kr.kro.dokbaro.server.core.termsofservice.domain.TermsOfService
import kr.kro.dokbaro.server.core.termsofservice.query.AgreeAllRequired
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class TermsOfServiceQueryService(
	private val readMemberAgreeTermsOfServicePort: ReadMemberAgreeTermsOfServicePort,
) : FindAgreeAllRequiredTermsOfServiceUseCase {
	override fun findBy(authId: UUID): AgreeAllRequired {
		val memberId: Long = TODO()

		val agrees: Collection<MemberAgreeTermsOfServiceElement> = readMemberAgreeTermsOfServicePort.findAll(memberId)
		val requiredTermsOfServiceIds: Collection<Long> = TermsOfService.entries.filter { it.primary }.map { it.id }

		return AgreeAllRequired(agrees.map { it.termsOfServiceId }.containsAll(requiredTermsOfServiceIds))
	}
}