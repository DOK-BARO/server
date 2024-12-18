package kr.kro.dokbaro.server.core.bookquiz.query

import java.time.LocalDateTime

data class UnsolvedGroupBookQuizSummary(
	val book: Book,
	val quiz: Quiz,
) {
	data class Book(
		val id: Long,
		val title: String,
		val imageUrl: String,
	)

	data class Quiz(
		val id: Long,
		val title: String,
		val description: String,
		val creator: Creator,
		val createdAt: LocalDateTime,
		val contributors: Collection<Contributor>,
	)

	data class Creator(
		val id: Long,
		val nickname: String,
		val profileImageUrl: String?,
	)

	data class Contributor(
		val id: Long,
		val nickname: String,
		val profileImageUrl: String?,
	)
}