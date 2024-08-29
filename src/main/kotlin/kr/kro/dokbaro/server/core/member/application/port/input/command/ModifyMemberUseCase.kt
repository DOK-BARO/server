package kr.kro.dokbaro.server.core.member.application.port.input.command

import kr.kro.dokbaro.server.core.member.application.port.input.dto.ModifyMemberCommand

fun interface ModifyMemberUseCase {
	fun modify(command: ModifyMemberCommand)
}