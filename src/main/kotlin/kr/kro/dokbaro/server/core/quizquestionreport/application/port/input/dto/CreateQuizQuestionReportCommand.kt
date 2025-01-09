package kr.kro.dokbaro.server.core.quizquestionreport.application.port.input.dto

data class CreateQuizQuestionReportCommand(
	val questionId: Long,
	val reporterId: Long,
	val contents: Collection<String>,
)