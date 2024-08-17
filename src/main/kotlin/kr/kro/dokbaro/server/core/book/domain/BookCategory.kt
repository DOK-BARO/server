package kr.kro.dokbaro.server.core.book.domain

data class BookCategory(
	val id: Long,
	val name: String,
	val details: Collection<BookCategory> = setOf(),
) {
	companion object {
		val ROOT_ID = 1L
	}
}