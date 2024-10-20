package kr.kro.dokbaro.server.core.quizreview.query

data class QuizReviewTotalScore(
	val quizId: Long,
	val averageStarRating: Double,
	val difficulty: Map<Int, TotalDifficultyScore>,
)

data class TotalDifficultyScore(
	val selectCount: Int,
	val selectRate: Double,
)