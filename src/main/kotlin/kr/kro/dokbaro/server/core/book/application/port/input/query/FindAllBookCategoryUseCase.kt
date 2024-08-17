package kr.kro.dokbaro.server.core.book.application.port.input.query

import kr.kro.dokbaro.server.core.book.domain.BookCategory

fun interface FindAllBookCategoryUseCase {
	fun findAllCategory(id: Long?): BookCategory
}