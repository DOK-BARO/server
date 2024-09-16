package kr.kro.dokbaro.server.core.book.application.port.out

import kr.kro.dokbaro.server.core.book.query.BookCategoryTree

fun interface ReadBookCategoryPort {
	fun findTreeBy(id: Long): BookCategoryTree
}