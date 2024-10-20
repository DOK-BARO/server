package kr.kro.dokbaro.server.core.quizreview.application.port.out.dto

data class QuizReviewTotalScoreElement(
	val quizId: Long,
	val score: Int,
	val difficultyLevel: Int,
)