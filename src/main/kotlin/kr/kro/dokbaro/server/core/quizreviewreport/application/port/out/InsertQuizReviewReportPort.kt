package kr.kro.dokbaro.server.core.quizreviewreport.application.port.out

import kr.kro.dokbaro.server.core.quizreviewreport.domain.QuizReviewReport

fun interface InsertQuizReviewReportPort {
	fun insert(quizReviewReport: QuizReviewReport): Long
}