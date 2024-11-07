package kr.kro.dokbaro.server.core.studygroup.application.port.input

import kr.kro.dokbaro.server.core.studygroup.query.StudyGroupDetail

fun interface FindStudyGroupDetailUseCase {
	fun findStudyGroupDetailBy(id: Long): StudyGroupDetail
}