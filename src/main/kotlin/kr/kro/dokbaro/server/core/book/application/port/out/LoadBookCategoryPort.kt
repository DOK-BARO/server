package kr.kro.dokbaro.server.core.book.application.port.out

import kr.kro.dokbaro.server.core.book.domain.BookCategory

fun interface LoadBookCategoryPort {
	fun getBookCategory(id: Long): BookCategory
}