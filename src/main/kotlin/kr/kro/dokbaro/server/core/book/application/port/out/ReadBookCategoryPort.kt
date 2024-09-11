package kr.kro.dokbaro.server.core.book.application.port.out

import kr.kro.dokbaro.server.core.book.domain.BookCategory

fun interface ReadBookCategoryPort {
	fun getBookCategory(id: Long): BookCategory
}