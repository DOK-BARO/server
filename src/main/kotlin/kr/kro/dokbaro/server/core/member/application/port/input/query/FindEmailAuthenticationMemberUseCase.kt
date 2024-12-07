package kr.kro.dokbaro.server.core.member.application.port.input.query

import kr.kro.dokbaro.server.core.member.query.EmailAuthenticationMember

fun interface FindEmailAuthenticationMemberUseCase {
	fun findEmailAuthenticationMember(email: String): EmailAuthenticationMember
}