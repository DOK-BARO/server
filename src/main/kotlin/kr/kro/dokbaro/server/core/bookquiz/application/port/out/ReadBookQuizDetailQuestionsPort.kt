package kr.kro.dokbaro.server.core.bookquiz.application.port.out

import kr.kro.dokbaro.server.core.bookquiz.application.port.out.dto.BookQuizDetailQuestions

fun interface ReadBookQuizDetailQuestionsPort {
	fun findBookQuizDetailBy(id: Long): BookQuizDetailQuestions?
}