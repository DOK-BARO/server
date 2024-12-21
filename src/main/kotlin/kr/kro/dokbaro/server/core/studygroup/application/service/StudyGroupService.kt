package kr.kro.dokbaro.server.core.studygroup.application.service

import kr.kro.dokbaro.server.core.studygroup.application.port.input.ChangeStudyGroupLeaderUseCase
import kr.kro.dokbaro.server.core.studygroup.application.port.input.CreateStudyGroupUseCase
import kr.kro.dokbaro.server.core.studygroup.application.port.input.DeleteStudyGroupUseCase
import kr.kro.dokbaro.server.core.studygroup.application.port.input.JoinStudyGroupUseCase
import kr.kro.dokbaro.server.core.studygroup.application.port.input.UpdateStudyGroupUseCase
import kr.kro.dokbaro.server.core.studygroup.application.port.input.dto.ChangeStudyGroupLeaderCommand
import kr.kro.dokbaro.server.core.studygroup.application.port.input.dto.CreateStudyGroupCommand
import kr.kro.dokbaro.server.core.studygroup.application.port.input.dto.JoinStudyGroupCommand
import kr.kro.dokbaro.server.core.studygroup.application.port.input.dto.UpdateStudyGroupCommand
import kr.kro.dokbaro.server.core.studygroup.application.port.out.DeleteStudyGroupPort
import kr.kro.dokbaro.server.core.studygroup.application.port.out.InsertStudyGroupPort
import kr.kro.dokbaro.server.core.studygroup.application.port.out.LoadStudyGroupPort
import kr.kro.dokbaro.server.core.studygroup.application.port.out.UpdateStudyGroupPort
import kr.kro.dokbaro.server.core.studygroup.application.port.out.dto.FindStudyGroupCondition
import kr.kro.dokbaro.server.core.studygroup.application.service.auth.StudyGroupAuthorityCheckService
import kr.kro.dokbaro.server.core.studygroup.application.service.exception.NotFoundStudyGroupException
import kr.kro.dokbaro.server.core.studygroup.domain.StudyGroup
import kr.kro.dokbaro.server.core.studygroup.event.JoinedStudyGroupMemberEvent
import kr.kro.dokbaro.server.security.details.DokbaroUser
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service

@Service
class StudyGroupService(
	private val insertStudyGroupPort: InsertStudyGroupPort,
	private val inviteCodeGenerator: InviteCodeGenerator,
	private val loadStudyGroupPort: LoadStudyGroupPort,
	private val updateStudyGroupPort: UpdateStudyGroupPort,
	private val eventPublisher: ApplicationEventPublisher,
	private val deleteStudyGroupPort: DeleteStudyGroupPort,
	private val studyGroupAuthorityCheckService: StudyGroupAuthorityCheckService,
) : CreateStudyGroupUseCase,
	JoinStudyGroupUseCase,
	UpdateStudyGroupUseCase,
	DeleteStudyGroupUseCase,
	ChangeStudyGroupLeaderUseCase {
	override fun create(command: CreateStudyGroupCommand): Long =
		insertStudyGroupPort.insert(
			StudyGroup.of(
				name = command.name,
				introduction = command.introduction,
				profileImageUrl = command.profileImageUrl,
				creatorId = command.creatorId,
				inviteCode = inviteCodeGenerator.generate(),
			),
		)

	override fun join(command: JoinStudyGroupCommand) {
		val studyGroup: StudyGroup =
			loadStudyGroupPort.findBy(
				FindStudyGroupCondition(
					inviteCode = command.inviteCode,
				),
			) ?: throw NotFoundStudyGroupException()

		studyGroup.join(participantId = command.memberId)

		updateStudyGroupPort.update(studyGroup)

		eventPublisher.publishEvent(
			JoinedStudyGroupMemberEvent(
				studyGroupId = studyGroup.id,
				studyGroupName = studyGroup.name,
				memberId = command.memberId,
				memberName = command.memberNickname,
			),
		)
	}

	override fun update(
		command: UpdateStudyGroupCommand,
		user: DokbaroUser,
	) {
		val studyGroup: StudyGroup =
			loadStudyGroupPort.findBy(FindStudyGroupCondition(id = command.id)) ?: throw NotFoundStudyGroupException()

		studyGroupAuthorityCheckService.checkUpdateStudyGroup(user, studyGroup)

		studyGroup.modify(
			name = command.name,
			introduction = command.introduction,
			profileImageUrl = command.profileImageUrl,
		)

		updateStudyGroupPort.update(studyGroup)
	}

	override fun deleteStudyGroup(
		id: Long,
		user: DokbaroUser,
	) {
		studyGroupAuthorityCheckService.checkDeleteStudyGroup(user, id)
		deleteStudyGroupPort.deleteStudyGroup(id)
	}

	override fun changeStudyGroupLeader(
		command: ChangeStudyGroupLeaderCommand,
		user: DokbaroUser,
	) {
		val studyGroup: StudyGroup =
			loadStudyGroupPort.findBy(FindStudyGroupCondition(id = command.studyGroupId)) ?: throw NotFoundStudyGroupException()

		studyGroupAuthorityCheckService.checkUpdateStudyGroup(user, studyGroup)

		studyGroup.changeStudyLeader(command.studyGroupId)

		updateStudyGroupPort.update(studyGroup)
	}
}