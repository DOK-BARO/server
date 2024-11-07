package kr.kro.dokbaro.server.core.studygroup.application.service

import kr.kro.dokbaro.server.core.member.application.port.input.query.FindCertificatedMemberUseCase
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
import java.util.UUID

@Service
class StudyGroupQueryService(
	private val findAllMyStudyGroupUseCase: FindCertificatedMemberUseCase,
	private val readStudyGroupCollectionPort: ReadStudyGroupCollectionPort,
	private val readStudyGroupMemberCollectionPort: ReadStudyGroupMemberCollectionPort,
	private val findStudyGroupDetailPort: ReadStudyGroupDetailPort,
) : FindAllMyStudyGroupUseCase,
	FindAllStudyGroupMembersUseCase,
	FindStudyGroupDetailUseCase {
	override fun findAll(certificationId: UUID): Collection<StudyGroupSummary> {
		val memberId: Long = findAllMyStudyGroupUseCase.getByCertificationId(certificationId).id

		return readStudyGroupCollectionPort.findAllByStudyMemberId(memberId)
	}

	override fun findAllStudyGroupMembers(id: Long): Collection<StudyGroupMemberResult> =
		readStudyGroupMemberCollectionPort.findAllStudyGroupMembers(id)

	override fun findStudyGroupDetailBy(id: Long): StudyGroupDetail =
		findStudyGroupDetailPort.findStudyGroupDetailBy(id) ?: throw NotFoundStudyGroupException()
}