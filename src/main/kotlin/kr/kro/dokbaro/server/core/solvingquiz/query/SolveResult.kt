package kr.kro.dokbaro.server.core.solvingquiz.query

data class SolveResult(
	val solvingQuizId: Long,
	val playerId: Long,
	val quizId: Long,
	val questionId: Long,
	val correct: Boolean,
	val correctAnswer: Collection<String>,
	val answerExplanationContent: String,
	val answerExplanationImages: Collection<String>,
)