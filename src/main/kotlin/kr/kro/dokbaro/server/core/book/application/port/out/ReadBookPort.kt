package kr.kro.dokbaro.server.core.book.application.port.out

import kr.kro.dokbaro.server.core.book.query.BookDetail

fun interface ReadBookPort {
	fun findBy(id: Long): BookDetail?
}