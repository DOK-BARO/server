package kr.kro.dokbaro.server.core.notification.application.port.input.dto

data class CreateNewStudyGroupMemberNotificationCommand(
	val studyGroupId: Long,
	val studyGroupName: String,
	val memberId: Long,
	val memberName: String,
)