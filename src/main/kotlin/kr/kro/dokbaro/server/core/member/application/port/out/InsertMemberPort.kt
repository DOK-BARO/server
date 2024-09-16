package kr.kro.dokbaro.server.core.member.application.port.out

import kr.kro.dokbaro.server.core.member.domain.Member

fun interface InsertMemberPort {
	fun insert(member: Member): Member
}