package kr.kro.dokbaro.server.core.studygroup.application.port.input

fun interface DeleteStudyGroupUseCase {
	fun deleteStudyGroup(id: Long)
}