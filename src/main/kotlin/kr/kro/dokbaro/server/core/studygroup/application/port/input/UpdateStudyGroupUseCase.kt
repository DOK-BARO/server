package kr.kro.dokbaro.server.core.studygroup.application.port.input

import kr.kro.dokbaro.server.core.studygroup.application.port.input.dto.UpdateStudyGroupCommand
import kr.kro.dokbaro.server.security.details.DokbaroUser

fun interface UpdateStudyGroupUseCase {
	fun update(
		command: UpdateStudyGroupCommand,
		user: DokbaroUser,
	)
}