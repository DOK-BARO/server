package kr.kro.dokbaro.server.core.quizreviewreport.application.port.input.dto

data class CreateQuizReviewReportCommand(
	val reporterId: Long,
	val quizReviewId: Long,
	val content: String,
)