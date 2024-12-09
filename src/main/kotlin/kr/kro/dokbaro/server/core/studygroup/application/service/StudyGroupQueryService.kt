package kr.kro.dokbaro.server.core.studygroup.application.service

import kr.kro.dokbaro.server.core.studygroup.application.port.input.FindAllMyStudyGroupUseCase
import kr.kro.dokbaro.server.core.studygroup.application.port.input.FindAllStudyGroupMembersUseCase
import kr.kro.dokbaro.server.core.studygroup.application.port.input.FindStudyGroupDetailUseCase
import kr.kro.dokbaro.server.core.studygroup.application.port.out.ReadStudyGroupCollectionPort
import kr.kro.dokbaro.server.core.studygroup.application.port.out.ReadStudyGroupDetailPort
import kr.kro.dokbaro.server.core.studygroup.application.port.out.ReadStudyGroupMemberCollectionPort
import kr.kro.dokbaro.server.core.studygroup.application.service.exception.NotFoundStudyGroupException
import kr.kro.dokbaro.server.core.studygroup.query.StudyGroupDetail
import kr.kro.dokbaro.server.core.studygroup.query.StudyGroupMemberResult
import kr.kro.dokbaro.server.core.studygroup.query.StudyGroupSummary
import org.springframework.stereotype.Service

@Service
class StudyGroupQueryService(
	private val readStudyGroupCollectionPort: ReadStudyGroupCollectionPort,
	private val readStudyGroupMemberCollectionPort: ReadStudyGroupMemberCollectionPort,
	private val findStudyGroupDetailPort: ReadStudyGroupDetailPort,
) : FindAllMyStudyGroupUseCase,
	FindAllStudyGroupMembersUseCase,
	FindStudyGroupDetailUseCase {
	override fun findAll(memberId: Long): Collection<StudyGroupSummary> =
		readStudyGroupCollectionPort.findAllByStudyMemberId(memberId)

	override fun findAllStudyGroupMembers(id: Long): Collection<StudyGroupMemberResult> =
		readStudyGroupMemberCollectionPort.findAllStudyGroupMembers(id)

	override fun findStudyGroupDetailBy(id: Long): StudyGroupDetail =
		findStudyGroupDetailPort.findStudyGroupDetailBy(id) ?: throw NotFoundStudyGroupException()
}