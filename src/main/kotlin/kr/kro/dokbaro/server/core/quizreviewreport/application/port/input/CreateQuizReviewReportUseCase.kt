package kr.kro.dokbaro.server.core.quizreviewreport.application.port.input

import kr.kro.dokbaro.server.core.quizreviewreport.application.port.input.dto.CreateQuizReviewReportCommand

fun interface CreateQuizReviewReportUseCase {
	fun create(command: CreateQuizReviewReportCommand): Long
}