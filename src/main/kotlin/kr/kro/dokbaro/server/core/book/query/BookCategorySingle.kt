package kr.kro.dokbaro.server.core.book.query

data class BookCategorySingle(
	val id: Long,
	val name: String,
	val parent: BookCategorySingle?,
)