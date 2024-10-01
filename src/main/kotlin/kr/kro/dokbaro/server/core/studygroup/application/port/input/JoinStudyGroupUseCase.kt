package kr.kro.dokbaro.server.core.studygroup.application.port.input

import kr.kro.dokbaro.server.core.studygroup.application.port.input.dto.JoinStudyGroupCommand

fun interface JoinStudyGroupUseCase {
	fun join(command: JoinStudyGroupCommand)
}