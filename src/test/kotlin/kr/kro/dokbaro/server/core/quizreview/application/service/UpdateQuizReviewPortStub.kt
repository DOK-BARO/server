package kr.kro.dokbaro.server.core.quizreview.application.service

import kr.kro.dokbaro.server.core.quizreview.application.port.out.UpdateQuizReviewPort
import kr.kro.dokbaro.server.core.quizreview.domain.QuizReview

class UpdateQuizReviewPortStub(
	var storage: QuizReview? = null,
) : UpdateQuizReviewPort {
	override fun update(quizReview: QuizReview) {
		storage = quizReview
	}

	fun clear() {
		storage = null
	}
}