package kr.kro.dokbaro.server.core.studygroup.application.port.input

import kr.kro.dokbaro.server.core.studygroup.application.port.input.dto.WithdrawStudyGroupMemberCommand
import kr.kro.dokbaro.server.security.details.DokbaroUser

fun interface WithdrawStudyGroupMemberUseCase {
	fun withdraw(
		command: WithdrawStudyGroupMemberCommand,
		user: DokbaroUser,
	)
}