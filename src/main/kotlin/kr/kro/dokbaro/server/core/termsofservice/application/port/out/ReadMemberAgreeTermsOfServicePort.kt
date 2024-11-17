package kr.kro.dokbaro.server.core.termsofservice.application.port.out

import kr.kro.dokbaro.server.core.termsofservice.application.port.out.dto.MemberAgreeTermsOfServiceElement

fun interface ReadMemberAgreeTermsOfServicePort {
	fun findAll(memberId: Long): Collection<MemberAgreeTermsOfServiceElement>
}