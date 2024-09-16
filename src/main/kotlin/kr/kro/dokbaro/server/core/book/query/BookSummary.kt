package kr.kro.dokbaro.server.core.book.query

data class BookSummary(
	val id: Long,
	val title: String,
	val publisher: String,
	val imageUrl: String?,
	val authors: Collection<String>,
)