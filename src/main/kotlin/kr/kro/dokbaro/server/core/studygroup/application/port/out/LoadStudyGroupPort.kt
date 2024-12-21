package kr.kro.dokbaro.server.core.studygroup.application.port.out

import kr.kro.dokbaro.server.core.studygroup.application.port.out.dto.FindStudyGroupCondition
import kr.kro.dokbaro.server.core.studygroup.domain.StudyGroup

fun interface LoadStudyGroupPort {
	fun findBy(condition: FindStudyGroupCondition): StudyGroup?
}