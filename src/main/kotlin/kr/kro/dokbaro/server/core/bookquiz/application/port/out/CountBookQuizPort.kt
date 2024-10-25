package kr.kro.dokbaro.server.core.bookquiz.application.port.out

fun interface CountBookQuizPort {
	fun countBookQuizBy(bookId: Long): Long
}