package kr.kro.dokbaro.server.core.termsofservice.application.port.out

import kr.kro.dokbaro.server.core.termsofservice.query.TermsOfServiceDetail

fun interface LoadTermsOfServiceDetailPort {
	fun getDetail(id: Long): TermsOfServiceDetail?
}