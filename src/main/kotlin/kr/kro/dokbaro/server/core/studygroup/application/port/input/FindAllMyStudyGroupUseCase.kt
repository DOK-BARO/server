package kr.kro.dokbaro.server.core.studygroup.application.port.input

import kr.kro.dokbaro.server.core.studygroup.query.StudyGroupSummary
import java.util.UUID

fun interface FindAllMyStudyGroupUseCase {
	fun findAll(certificationId: UUID): Collection<StudyGroupSummary>
}