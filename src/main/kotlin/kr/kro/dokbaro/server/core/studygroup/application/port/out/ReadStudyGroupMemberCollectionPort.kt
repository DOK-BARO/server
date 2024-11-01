package kr.kro.dokbaro.server.core.studygroup.application.port.out

import kr.kro.dokbaro.server.core.studygroup.query.StudyGroupMemberResult

interface ReadStudyGroupMemberCollectionPort {
	fun findAllStudyGroupMembers(id: Long): Collection<StudyGroupMemberResult>
}