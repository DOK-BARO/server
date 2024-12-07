package kr.kro.dokbaro.server.core.member.application.port.input.command

import kr.kro.dokbaro.server.core.member.application.port.input.command.dto.RegisterMemberCommand
import kr.kro.dokbaro.server.core.member.domain.Member

fun interface RegisterMemberUseCase {
	fun register(command: RegisterMemberCommand): Member
}