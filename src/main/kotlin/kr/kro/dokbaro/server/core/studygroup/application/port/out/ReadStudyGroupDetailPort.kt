package kr.kro.dokbaro.server.core.studygroup.application.port.out

import kr.kro.dokbaro.server.core.studygroup.query.StudyGroupDetail

fun interface ReadStudyGroupDetailPort {
	fun findStudyGroupDetailBy(id: Long): StudyGroupDetail?
}