package kr.kro.dokbaro.server.core.quizreview.adapter.input.web.dto

data class CreateQuizReviewRequest(
	val starRating: Int,
	val difficultyLevel: Int,
	val comment: String?,
	val quizId: Long,
)