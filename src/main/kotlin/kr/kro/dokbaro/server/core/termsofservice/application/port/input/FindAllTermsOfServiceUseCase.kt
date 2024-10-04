package kr.kro.dokbaro.server.core.termsofservice.application.port.input

import kr.kro.dokbaro.server.core.termsofservice.query.TermsOfServiceSummary

fun interface FindAllTermsOfServiceUseCase {
	fun findAll(): Collection<TermsOfServiceSummary>
}