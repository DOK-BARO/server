package kr.kro.dokbaro.server.core.studygroup.application.port.out

import kr.kro.dokbaro.server.core.studygroup.application.port.out.dto.CountStudyGroupCondition

fun interface CountStudyGroupPort {
	fun countBy(condition: CountStudyGroupCondition): Long
}