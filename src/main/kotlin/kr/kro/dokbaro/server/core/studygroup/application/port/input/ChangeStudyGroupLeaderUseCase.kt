package kr.kro.dokbaro.server.core.studygroup.application.port.input

import kr.kro.dokbaro.server.core.studygroup.application.port.input.dto.ChangeStudyGroupLeaderCommand
import kr.kro.dokbaro.server.security.details.DokbaroUser

fun interface ChangeStudyGroupLeaderUseCase {
	fun changeStudyGroupLeader(
		command: ChangeStudyGroupLeaderCommand,
		user: DokbaroUser,
	)
}