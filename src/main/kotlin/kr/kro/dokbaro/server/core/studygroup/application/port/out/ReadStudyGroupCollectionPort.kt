package kr.kro.dokbaro.server.core.studygroup.application.port.out

import kr.kro.dokbaro.server.core.studygroup.query.StudyGroupSummary

fun interface ReadStudyGroupCollectionPort {
	fun findAllByStudyMemberId(memberId: Long): Collection<StudyGroupSummary>
}