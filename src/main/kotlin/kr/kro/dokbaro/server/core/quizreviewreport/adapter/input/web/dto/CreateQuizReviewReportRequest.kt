package kr.kro.dokbaro.server.core.quizreviewreport.adapter.input.web.dto

data class CreateQuizReviewReportRequest(
	val quizReviewId: Long,
	val content: String,
)