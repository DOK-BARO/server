package kr.kro.dokbaro.server.core.studygroup.application.port.input

import kr.kro.dokbaro.server.security.details.DokbaroUser

fun interface DeleteStudyGroupUseCase {
	fun deleteStudyGroup(
		id: Long,
		user: DokbaroUser,
	)
}