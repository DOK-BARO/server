package kr.kro.dokbaro.server.core.termsofservice.application.port.input

import kr.kro.dokbaro.server.core.termsofservice.query.TermsOfServiceDetail

fun interface FindTermsOfServiceDetailUseCase {
	fun findDetail(id: Long): TermsOfServiceDetail
}