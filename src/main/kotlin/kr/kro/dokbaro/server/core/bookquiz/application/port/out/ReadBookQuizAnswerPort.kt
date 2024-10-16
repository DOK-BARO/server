package kr.kro.dokbaro.server.core.bookquiz.application.port.out

import kr.kro.dokbaro.server.core.bookquiz.query.BookQuizAnswer

interface ReadBookQuizAnswerPort {
	fun findBookQuizAnswerBy(questionId: Long): BookQuizAnswer?
}