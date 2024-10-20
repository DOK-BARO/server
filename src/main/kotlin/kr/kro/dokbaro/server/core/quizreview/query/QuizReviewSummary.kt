package kr.kro.dokbaro.server.core.quizreview.query

import java.time.Instant

data class QuizReviewSummary(
	val id: Long,
	val quizId: Long,
	val starRating: Int,
	val difficultyLevel: Int,
	val writerId: Long,
	val writerNickname: String,
	val comment: String?,
	val createdAt: Instant,
)