package kr.kro.dokbaro.server.core.book.domain

data class BookCategory(
	val id: Long = UNSAVED_BOOK_CATEGORY_ID,
	val name: String,
	val details: Collection<BookCategory> = setOf(),
) {
	companion object {
		const val ROOT_ID = 1L
		private const val UNSAVED_BOOK_CATEGORY_ID = 0L
	}
}