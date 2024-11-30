package kr.kro.dokbaro.server.core.bookquiz.application.port.out

import kr.kro.dokbaro.server.core.bookquiz.query.BookQuizExplanation

fun interface ReadBookQuizExplanationPort {
	fun findExplanationBy(id: Long): BookQuizExplanation?
}