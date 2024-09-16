package kr.kro.dokbaro.server.core.book.domain

import kr.kro.dokbaro.server.common.constant.Constants

data class BookCategory(
	val id: Long = Constants.UNSAVED_ID,
	val name: String,
	val details: Collection<BookCategory> = listOf(),
) {
	companion object {
		const val ROOT_ID = 1L
	}
}