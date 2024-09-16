package kr.kro.dokbaro.server.core.book.application.port.input

import kr.kro.dokbaro.server.core.book.query.BookCategoryTree

fun interface FindAllBookCategoryUseCase {
	fun getTree(id: Long?): BookCategoryTree
}