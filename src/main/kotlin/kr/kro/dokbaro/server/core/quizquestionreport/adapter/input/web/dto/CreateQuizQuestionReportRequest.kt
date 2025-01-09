package kr.kro.dokbaro.server.core.quizquestionreport.adapter.input.web.dto

data class CreateQuizQuestionReportRequest(
	val questionId: Long,
	val contents: Collection<String>,
)