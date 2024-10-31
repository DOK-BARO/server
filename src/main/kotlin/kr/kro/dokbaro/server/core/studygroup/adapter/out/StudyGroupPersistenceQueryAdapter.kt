package kr.kro.dokbaro.server.core.studygroup.adapter.out

import kr.kro.dokbaro.server.common.annotation.PersistenceAdapter
import kr.kro.dokbaro.server.core.studygroup.adapter.out.persistence.repository.jooq.StudyGroupQueryRepository
import kr.kro.dokbaro.server.core.studygroup.application.port.out.ReadStudyGroupCollectionPort
import kr.kro.dokbaro.server.core.studygroup.application.port.out.ReadStudyGroupMemberCollectionPort
import kr.kro.dokbaro.server.core.studygroup.query.StudyGroupMemberResult
import kr.kro.dokbaro.server.core.studygroup.query.StudyGroupSummary

@PersistenceAdapter
class StudyGroupPersistenceQueryAdapter(
	private val studyGroupQueryRepository: StudyGroupQueryRepository,
) : ReadStudyGroupCollectionPort,
	ReadStudyGroupMemberCollectionPort {
	override fun findAllByStudyMemberId(memberId: Long): Collection<StudyGroupSummary> =
		studyGroupQueryRepository.findAllByStudyMemberId(memberId)

	override fun findAllStudyGroupMembers(id: Long): Collection<StudyGroupMemberResult> =
		studyGroupQueryRepository.findAllStudyGroupMembers(id)
}