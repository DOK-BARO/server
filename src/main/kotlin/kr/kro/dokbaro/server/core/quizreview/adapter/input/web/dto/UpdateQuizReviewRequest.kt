package kr.kro.dokbaro.server.core.quizreview.adapter.input.web.dto

data class UpdateQuizReviewRequest(
	val starRating: Int,
	val difficultyLevel: Int,
	val comment: String?,
)