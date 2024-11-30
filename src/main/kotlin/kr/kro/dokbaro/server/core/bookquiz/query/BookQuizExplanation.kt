package kr.kro.dokbaro.server.core.bookquiz.query

import java.time.LocalDateTime

data class BookQuizExplanation(
	val id: Long,
	val title: String,
	val description: String,
	val createdAt: LocalDateTime,
	val creator: Creator,
	val book: Book,
) {
	data class Creator(
		val id: Long,
		val nickname: String,
		val profileImageUrl: String?,
	)

	data class Book(
		val id: Long,
		val title: String,
		val imageUrl: String?,
	)
}