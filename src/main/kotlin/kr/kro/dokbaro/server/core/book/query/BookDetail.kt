package kr.kro.dokbaro.server.core.book.query

data class BookDetail(
	val id: Long,
	val isbn: String,
	val title: String,
	val publisher: String,
	val description: String?,
	val imageUrl: String?,
	val categories: Collection<BookCategorySingle>,
	val authors: Collection<String>,
)