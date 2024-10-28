package kr.kro.dokbaro.server.core.solvingquiz.query

data class TotalGradeResult(
	val solvingQuizId: Long,
	val quizId: Long,
	val playerId: Long,
	val questionCount: Int,
	val correctCount: Int,
)