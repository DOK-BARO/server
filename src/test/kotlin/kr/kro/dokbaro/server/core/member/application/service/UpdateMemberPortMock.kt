package kr.kro.dokbaro.server.core.member.application.service

import kr.kro.dokbaro.server.core.member.application.port.out.UpdateMemberPort
import kr.kro.dokbaro.server.core.member.domain.Member

class UpdateMemberPortMock(
	var storage: Member? = null,
) : UpdateMemberPort {
	override fun update(member: Member) {
		storage = member
	}
}