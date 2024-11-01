package kr.kro.dokbaro.server.core.notification.domain.content

import kr.kro.dokbaro.server.core.notification.domain.ContentStrategy

class NewStudyGroupMemberNotificationContent(
	private val studyGroupName: String,
	private val memberName: String,
) : ContentStrategy {
	override fun getContent(): String = "[$studyGroupName] 에 ${memberName}님이 가입하셨습니다"
}