package kr.kro.dokbaro.server.core.bookquiz.event

data class CreatedQuizEvent(
	val quizId: Long,
	val creatorId: Long,
	val creatorName: String,
	val studyGroupId: Long?,
)