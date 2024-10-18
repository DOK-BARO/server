package kr.kro.dokbaro.server.core.quizreview.adapter.input.web.dto

data class CreateQuizReviewRequest(
	val score: Int,
	val difficultyLevel: Int,
	val comment: String?,
	val quizId: Long,
)