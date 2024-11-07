package kr.kro.dokbaro.server.core.bookquiz.query

import java.time.LocalDateTime

data class MyBookQuizSummary(
	val id: Long,
	val bookImageUrl: String?,
	val title: String,
	val updatedAt: LocalDateTime,
)