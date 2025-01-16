package kr.kro.dokbaro.server.core.quizreview.query

data class MyQuizReview(
	val id: Long,
	var starRating: Int,
	var difficultyLevel: Int,
	var comment: String? = null,
	val quizId: Long,
)