package kr.kro.dokbaro.server.core.book.application.port.out

import kr.kro.dokbaro.server.core.book.domain.Book

fun interface InsertBookPort {
	fun insert(book: Book): Long
}