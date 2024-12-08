package kr.kro.dokbaro.server.core.member.application.port.out

import kr.kro.dokbaro.server.core.member.query.EmailAuthenticationMember

fun interface ReadEmailAuthenticationMemberPort {
	fun findEmailAuthenticationMember(email: String): EmailAuthenticationMember?
}