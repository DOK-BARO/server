package kr.kro.dokbaro.server.core.quizquestionreport.application.port.out

import kr.kro.dokbaro.server.core.quizquestionreport.domain.QuizQuestionReport

fun interface InsertQuizQuestionReportPort {
	fun insert(quizQuestionReport: QuizQuestionReport): Long
}