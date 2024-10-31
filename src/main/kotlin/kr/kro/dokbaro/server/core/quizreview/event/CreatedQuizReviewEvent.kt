package kr.kro.dokbaro.server.core.quizreview.event

data class CreatedQuizReviewEvent(
	val quizId: Long,
	val reviewId: Long,
	val quizCreatorId: Long,
)