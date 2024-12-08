package kr.kro.dokbaro.server.core.quizreviewreport.application.port.input.dto

data class CreateQuizReviewReportCommand(
	val loginUserId: Long,
	val quizReviewId: Long,
	val content: String,
)