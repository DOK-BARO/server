package kr.kro.dokbaro.server.core.termsofservice.adapter.input.web

import kr.kro.dokbaro.server.common.util.UUIDUtils
import kr.kro.dokbaro.server.core.termsofservice.application.port.input.AgreeTermsOfServiceUseCase
import kr.kro.dokbaro.server.core.termsofservice.application.port.input.FindAllTermsOfServiceUseCase
import kr.kro.dokbaro.server.core.termsofservice.application.port.input.FindTermsOfServiceDetailUseCase
import kr.kro.dokbaro.server.core.termsofservice.application.port.input.dto.AgreeTermsOfServiceRequest
import kr.kro.dokbaro.server.core.termsofservice.query.TermsOfServiceDetail
import kr.kro.dokbaro.server.core.termsofservice.query.TermsOfServiceSummary
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/terms-of-services")
class TermsOfServiceController(
	private val findAllTermsOfServiceUseCase: FindAllTermsOfServiceUseCase,
	private val findTermsOfServiceDetailUseCase: FindTermsOfServiceDetailUseCase,
	private val agreeTermsOfServiceUseCase: AgreeTermsOfServiceUseCase,
) {
	@GetMapping
	fun getAllTermsOfServices(): Collection<TermsOfServiceSummary> = findAllTermsOfServiceUseCase.findAll()

	@GetMapping("/{id}/detail")
	fun getTermsOfServiceDetail(
		@PathVariable id: Long,
	): TermsOfServiceDetail = findTermsOfServiceDetailUseCase.findDetail(id)

	@PostMapping("/agree")
	fun agreeTermsOfService(
		auth: Authentication,
		@RequestBody body: AgreeTermsOfServiceRequest,
	) {
		agreeTermsOfServiceUseCase.agree(UUIDUtils.stringToUUID(auth.name), body.items)
	}
}