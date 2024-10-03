package kr.kro.dokbaro.server.core.studygroup.application.service

import kr.kro.dokbaro.server.core.member.application.port.input.query.FindCertificatedMemberUseCase
import kr.kro.dokbaro.server.core.studygroup.application.port.input.FindAllMyStudyGroupUseCase
import kr.kro.dokbaro.server.core.studygroup.application.port.out.ReadStudyGroupCollectionPort
import kr.kro.dokbaro.server.core.studygroup.query.StudyGroupSummary
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class StudyGroupQueryService(
	private val findAllMyStudyGroupUseCase: FindCertificatedMemberUseCase,
	private val readStudyGroupCollectionPort: ReadStudyGroupCollectionPort,
) : FindAllMyStudyGroupUseCase {
	override fun findAll(certificationId: UUID): Collection<StudyGroupSummary> {
		val memberId: Long = findAllMyStudyGroupUseCase.getByCertificationId(certificationId).id

		return readStudyGroupCollectionPort.findAllByStudyMemberId(memberId)
	}
}