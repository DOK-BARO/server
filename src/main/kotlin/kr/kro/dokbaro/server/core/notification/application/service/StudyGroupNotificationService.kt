package kr.kro.dokbaro.server.core.notification.application.service

import kr.kro.dokbaro.server.core.notification.application.port.input.CreateNewStudyGroupMemberNotificationUseCase
import kr.kro.dokbaro.server.core.notification.application.port.input.dto.CreateNewStudyGroupMemberNotificationCommand
import kr.kro.dokbaro.server.core.notification.application.port.out.InsertNotificationPort
import kr.kro.dokbaro.server.core.notification.application.port.out.InsertNotificationVisibilityPort
import kr.kro.dokbaro.server.core.notification.domain.Notification
import kr.kro.dokbaro.server.core.notification.domain.NotificationTrigger
import kr.kro.dokbaro.server.core.notification.domain.NotificationVisibility
import kr.kro.dokbaro.server.core.notification.domain.content.NewStudyGroupMemberNotificationContent
import kr.kro.dokbaro.server.core.studygroup.application.port.input.FindAllStudyGroupMembersUseCase
import kr.kro.dokbaro.server.core.studygroup.query.StudyGroupMemberResult
import org.springframework.stereotype.Service

@Service
class StudyGroupNotificationService(
	private val insertNotificationPort: InsertNotificationPort,
	private val insertNotificationVisibilityPort: InsertNotificationVisibilityPort,
	private val findAllStudyGroupMembersUseCase: FindAllStudyGroupMembersUseCase,
) : CreateNewStudyGroupMemberNotificationUseCase {
	override fun createNewStudyGroupMemberNotification(command: CreateNewStudyGroupMemberNotificationCommand) {
		val savedNotificationId =
			insertNotificationPort.insert(
				Notification(
					content =
						NewStudyGroupMemberNotificationContent(
							command.studyGroupName,
							command.memberName,
						).getContent(),
					trigger = NotificationTrigger.NEW_STUDY_GROUP_MEMBER,
					linkedId = command.studyGroupId,
				),
			)

		val studyGroupMembers: Collection<StudyGroupMemberResult> =
			findAllStudyGroupMembersUseCase.findAllStudyGroupMembers(
				command.studyGroupId,
			)

		insertNotificationVisibilityPort.insertAll(
			studyGroupMembers.map { NotificationVisibility(savedNotificationId, it.memberId) },
		)
	}
}