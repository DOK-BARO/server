package kr.kro.dokbaro.server.core.studygroup.application.port.out

import kr.kro.dokbaro.server.core.studygroup.domain.StudyGroup

fun interface SaveStudyGroupPort {
	fun save(studyGroup: StudyGroup): Long
}