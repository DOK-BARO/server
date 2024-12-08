package kr.kro.dokbaro.server.core.studygroup.application.service

import kr.kro.dokbaro.server.core.studygroup.application.port.input.CreateStudyGroupUseCase
import kr.kro.dokbaro.server.core.studygroup.application.port.input.JoinStudyGroupUseCase
import kr.kro.dokbaro.server.core.studygroup.application.port.input.dto.CreateStudyGroupCommand
import kr.kro.dokbaro.server.core.studygroup.application.port.input.dto.JoinStudyGroupCommand
import kr.kro.dokbaro.server.core.studygroup.application.port.out.InsertStudyGroupPort
import kr.kro.dokbaro.server.core.studygroup.application.port.out.LoadStudyGroupByInviteCodePort
import kr.kro.dokbaro.server.core.studygroup.application.port.out.UpdateStudyGroupPort
import kr.kro.dokbaro.server.core.studygroup.application.service.exception.NotFoundStudyGroupException
import kr.kro.dokbaro.server.core.studygroup.domain.StudyGroup
import kr.kro.dokbaro.server.core.studygroup.event.JoinedStudyGroupMemberEvent
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service

@Service
class StudyGroupService(
	private val insertStudyGroupPort: InsertStudyGroupPort,
	private val inviteCodeGenerator: InviteCodeGenerator,
	private val loadStudyGroupByInviteCodePort: LoadStudyGroupByInviteCodePort,
	private val updateStudyGroupPort: UpdateStudyGroupPort,
	private val eventPublisher: ApplicationEventPublisher,
) : CreateStudyGroupUseCase,
	JoinStudyGroupUseCase {
	override fun create(command: CreateStudyGroupCommand): Long {
		val creator = TODO()

		return insertStudyGroupPort.insert(
			StudyGroup.of(
				name = command.name,
				introduction = command.introduction,
				profileImageUrl = command.profileImageUrl,
				creatorId = TODO(),
				inviteCode = inviteCodeGenerator.generate(),
			),
		)
	}

	override fun join(command: JoinStudyGroupCommand) {
		val studyGroup: StudyGroup =
			loadStudyGroupByInviteCodePort.findByInviteCode(
				command.inviteCode,
			) ?: throw NotFoundStudyGroupException()
		val member = TODO()

		studyGroup.join(TODO())

		updateStudyGroupPort.update(studyGroup)

		eventPublisher.publishEvent(
			JoinedStudyGroupMemberEvent(
				studyGroupId = studyGroup.id,
				studyGroupName = studyGroup.name,
				memberId = TODO(),
				memberName = TODO(),
			),
		)
	}
}