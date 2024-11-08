package kr.kro.dokbaro.server.core.solvingquiz.query

import java.time.LocalDateTime

data class MySolveSummary(
	val id: Long,
	val solvedAt: LocalDateTime,
	val bookImageUrl: String,
	val quiz: MySolvingQuizSummary,
)

data class MySolvingQuizSummary(
	val id: Long,
	val title: String,
)