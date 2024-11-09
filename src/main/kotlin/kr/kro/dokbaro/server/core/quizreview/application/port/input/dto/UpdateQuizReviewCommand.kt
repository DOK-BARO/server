package kr.kro.dokbaro.server.core.quizreview.application.port.input.dto

data class UpdateQuizReviewCommand(
	val id: Long,
	val starRating: Int,
	val difficultyLevel: Int,
	val comment: String?,
)