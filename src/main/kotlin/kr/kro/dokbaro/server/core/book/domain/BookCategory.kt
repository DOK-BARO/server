package kr.kro.dokbaro.server.core.book.domain

data class BookCategory(
	val id: Long = Constants.UNSAVED_ID,
	val name: String,
	val details: Collection<BookCategory> = setOf(),
) {
	companion object {
		const val ROOT_ID = 1L
	}
}