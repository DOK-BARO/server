package kr.kro.dokbaro.server.core.studygroup.application.port.out

import kr.kro.dokbaro.server.core.studygroup.application.port.out.dto.FindStudyGroupCondition
import kr.kro.dokbaro.server.core.studygroup.query.StudyGroupDetail

fun interface ReadStudyGroupDetailPort {
	fun findStudyGroupDetailBy(condition: FindStudyGroupCondition): StudyGroupDetail?
}