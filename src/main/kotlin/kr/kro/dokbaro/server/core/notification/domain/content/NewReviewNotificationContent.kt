package kr.kro.dokbaro.server.core.notification.domain.content

import kr.kro.dokbaro.server.core.notification.domain.ContentStrategy

class NewReviewNotificationContent : ContentStrategy {
	override fun getContent(): String = "내가 만든 퀴즈에 새로운 후기가 달렸어요"
}