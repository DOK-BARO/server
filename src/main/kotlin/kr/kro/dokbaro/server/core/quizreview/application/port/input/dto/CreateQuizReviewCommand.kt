package kr.kro.dokbaro.server.core.quizreview.application.port.input.dto

import java.util.UUID

data class CreateQuizReviewCommand(
	val starRating: Int,
	val difficultyLevel: Int,
	val comment: String?,
	val authId: UUID,
	val quizId: Long,
)