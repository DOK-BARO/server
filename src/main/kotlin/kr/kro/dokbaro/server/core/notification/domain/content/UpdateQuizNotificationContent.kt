package kr.kro.dokbaro.server.core.notification.domain.content

import kr.kro.dokbaro.server.core.notification.domain.ContentStrategy

class UpdateQuizNotificationContent(
	private val quizTitle: String,
) : ContentStrategy {
	override fun getContent(): String = "[$quizTitle] 이 수정되었어요!"
}