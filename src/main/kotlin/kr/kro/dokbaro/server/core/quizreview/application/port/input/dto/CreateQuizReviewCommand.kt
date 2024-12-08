package kr.kro.dokbaro.server.core.quizreview.application.port.input.dto

data class CreateQuizReviewCommand(
	val starRating: Int,
	val difficultyLevel: Int,
	val comment: String?,
	val creatorId: Long,
	val quizId: Long,
)