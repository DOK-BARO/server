package kr.kro.dokbaro.server.core.studygroup.application.port.input

import kr.kro.dokbaro.server.core.studygroup.application.port.input.dto.CreateStudyGroupCommand

fun interface CreateStudyGroupUseCase {
	fun create(command: CreateStudyGroupCommand): Long
}