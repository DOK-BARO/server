package kr.kro.dokbaro.server.core.quizreview.application.port.out.dto

data class QuizReviewTotalScoreElement(
	val quizId: Long,
	val starRating: Int,
	val difficultyLevel: Int,
)