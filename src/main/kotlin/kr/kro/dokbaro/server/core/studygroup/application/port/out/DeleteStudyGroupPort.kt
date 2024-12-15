package kr.kro.dokbaro.server.core.studygroup.application.port.out

fun interface DeleteStudyGroupPort {
	fun deleteStudyGroup(id: Long)
}