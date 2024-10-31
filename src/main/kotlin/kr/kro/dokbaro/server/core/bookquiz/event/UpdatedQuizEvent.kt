package kr.kro.dokbaro.server.core.bookquiz.event

data class UpdatedQuizEvent(
	val quizId: Long,
	val quizTitle: String,
	val quizCreatorId: Long,
)