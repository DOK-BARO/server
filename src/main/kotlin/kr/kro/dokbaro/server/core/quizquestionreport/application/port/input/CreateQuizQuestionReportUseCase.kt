package kr.kro.dokbaro.server.core.quizquestionreport.application.port.input

import kr.kro.dokbaro.server.core.quizquestionreport.application.port.input.dto.CreateQuizQuestionReportCommand

fun interface CreateQuizQuestionReportUseCase {
	fun create(command: CreateQuizQuestionReportCommand): Long
}