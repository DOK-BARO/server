package kr.kro.dokbaro.server.core.studygroup.application.service

import kr.kro.dokbaro.server.core.member.application.port.input.dto.MemberResponse
import kr.kro.dokbaro.server.core.member.application.port.input.query.FindCertificatedMemberUseCase
import kr.kro.dokbaro.server.core.studygroup.application.port.input.CreateStudyGroupUseCase
import kr.kro.dokbaro.server.core.studygroup.application.port.input.dto.CreateStudyGroupCommand
import kr.kro.dokbaro.server.core.studygroup.application.port.out.SaveStudyGroupPort
import kr.kro.dokbaro.server.core.studygroup.domain.StudyGroup
import org.springframework.stereotype.Service

@Service
class StudyGroupService(
	private val saveStudyGroupPort: SaveStudyGroupPort,
	private val findCertificatedMemberUseCase: FindCertificatedMemberUseCase,
) : CreateStudyGroupUseCase {
	override fun create(command: CreateStudyGroupCommand): Long {
		val creator: MemberResponse = findCertificatedMemberUseCase.getByCertificationId(command.creatorAuthId)
		
		return saveStudyGroupPort.save(
			StudyGroup(
				command.name,
				command.introduction,
				command.profileImageUrl,
				creator.id,
			),
		)
	}
}