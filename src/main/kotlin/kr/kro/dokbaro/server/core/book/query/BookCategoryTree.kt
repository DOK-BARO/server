package kr.kro.dokbaro.server.core.book.query

data class BookCategoryTree(
	val id: Long,
	val name: String,
	val details: Collection<BookCategoryTree> = emptyList(),
)