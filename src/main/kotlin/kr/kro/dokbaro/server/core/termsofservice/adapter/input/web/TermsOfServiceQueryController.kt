package kr.kro.dokbaro.server.core.termsofservice.adapter.input.web

import kr.kro.dokbaro.server.common.util.UUIDUtils
import kr.kro.dokbaro.server.core.termsofservice.application.port.input.FindAgreeAllRequiredTermsOfServiceUseCase
import kr.kro.dokbaro.server.core.termsofservice.query.AgreeAllRequired
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/terms-of-services")
class TermsOfServiceQueryController(
	private val findAgreeAllRequiredTermsOfServiceUseCase: FindAgreeAllRequiredTermsOfServiceUseCase,
) {
	@GetMapping("/member-agree/required")
	fun getMemberAgreeTermsOfService(auth: Authentication): AgreeAllRequired =
		findAgreeAllRequiredTermsOfServiceUseCase.findBy(UUIDUtils.stringToUUID(auth.name))
}