package kr.kro.dokbaro.server.core.member.application.port.out

import kr.kro.dokbaro.server.core.member.domain.Member

fun interface UpdateMemberPort {
	fun update(member: Member)
}