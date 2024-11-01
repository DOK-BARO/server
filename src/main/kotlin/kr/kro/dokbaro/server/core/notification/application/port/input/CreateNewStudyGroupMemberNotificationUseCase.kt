package kr.kro.dokbaro.server.core.notification.application.port.input

import kr.kro.dokbaro.server.core.notification.application.port.input.dto.CreateNewStudyGroupMemberNotificationCommand

fun interface CreateNewStudyGroupMemberNotificationUseCase {
	fun createNewStudyGroupMemberNotification(command: CreateNewStudyGroupMemberNotificationCommand)
}