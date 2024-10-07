package kr.kro.dokbaro.server.core.bookquiz.application.port.out

import kr.kro.dokbaro.server.core.bookquiz.query.BookQuizQuestions

fun interface ReadBookQuizQuestionPort {
	fun findBookQuizQuestionsBy(quizId: Long): BookQuizQuestions?
}