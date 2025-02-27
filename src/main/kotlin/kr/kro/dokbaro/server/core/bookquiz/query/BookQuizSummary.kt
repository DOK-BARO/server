package kr.kro.dokbaro.server.core.bookquiz.query

data class BookQuizSummary(
	val id: Long,
	val title: String,
	val averageStarRating: Double,
	val averageDifficultyLevel: Double,
	val questionCount: Int,
	val reviewCount: Int,
	val creator: Creator,
	val temporary: Boolean,
) {
	data class Creator(
		val id: Long,
		val nickname: String,
		val profileUrl: String?,
	)
}