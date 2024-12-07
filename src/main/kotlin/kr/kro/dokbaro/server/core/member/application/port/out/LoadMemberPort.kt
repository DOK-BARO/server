package kr.kro.dokbaro.server.core.member.application.port.out

import kr.kro.dokbaro.server.core.member.domain.Member

fun interface LoadMemberPort {
	fun findBy(id: Long): Member?
}