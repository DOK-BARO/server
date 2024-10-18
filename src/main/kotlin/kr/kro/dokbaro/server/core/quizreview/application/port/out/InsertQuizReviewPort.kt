package kr.kro.dokbaro.server.core.quizreview.application.port.out

import kr.kro.dokbaro.server.core.quizreview.domain.QuizReview

fun interface InsertQuizReviewPort {
	fun insert(quizReview: QuizReview): Long
}