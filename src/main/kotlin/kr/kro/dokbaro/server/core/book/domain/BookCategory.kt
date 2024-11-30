package kr.kro.dokbaro.server.core.book.domain

import kr.kro.dokbaro.server.common.constant.Constants

data class BookCategory(
	val id: Long = Constants.UNSAVED_ID,
	val koreanName: String,
	val englishName: String,
	val parentId: Long? = null,
) {
	companion object {
		const val ROOT_ID = 1L
	}
}