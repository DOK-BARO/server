package kr.kro.dokbaro.server.core.bookquiz.application.port.out

import kr.kro.dokbaro.server.core.bookquiz.domain.QuizQuestion

interface InsertQuizQuestionPort {
	fun insert(quizQuestion: QuizQuestion): Long
}