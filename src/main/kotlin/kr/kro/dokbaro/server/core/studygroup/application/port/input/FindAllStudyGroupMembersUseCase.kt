package kr.kro.dokbaro.server.core.studygroup.application.port.input

import kr.kro.dokbaro.server.core.studygroup.query.StudyGroupMemberResult

fun interface FindAllStudyGroupMembersUseCase {
	fun findAllStudyGroupMembers(id: Long): Collection<StudyGroupMemberResult>
}