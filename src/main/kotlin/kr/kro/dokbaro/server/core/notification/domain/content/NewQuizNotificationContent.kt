package kr.kro.dokbaro.server.core.notification.domain.content

import kr.kro.dokbaro.server.core.notification.domain.ContentStrategy

data class NewQuizNotificationContent(
	private val memberName: String,
) : ContentStrategy {
	override fun getContent(): String = "${memberName}님이 새로운 퀴즈를 추가했어요"
}