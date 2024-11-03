package kr.kro.dokbaro.server.core.bookquiz.query

import java.time.LocalDateTime

data class BookAndBookQuizSummary(
	val book: BookSummary,
	val quiz: QuizSummary,
)

data class BookSummary(
	val id: Long,
	val title: String,
	val imageUrl: String,
)

data class QuizSummary(
	val id: Long,
	val title: String,
	val creator: QuizCreator,
	val createdAt: LocalDateTime,
)

data class QuizCreator(
	val id: Long,
	val nickname: String,
	val profileImageUrl: String?,
)