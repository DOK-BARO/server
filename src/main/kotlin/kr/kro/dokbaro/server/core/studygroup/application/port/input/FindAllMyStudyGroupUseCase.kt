package kr.kro.dokbaro.server.core.studygroup.application.port.input

import kr.kro.dokbaro.server.core.studygroup.query.StudyGroupSummary

fun interface FindAllMyStudyGroupUseCase {
	fun findAll(loginUserId: Long): Collection<StudyGroupSummary>
}