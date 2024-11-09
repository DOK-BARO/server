package kr.kro.dokbaro.server.core.quizreviewreport.application.port.input.dto

import java.util.UUID

data class CreateQuizReviewReportCommand(
	val authId: UUID,
	val quizReviewId: Long,
	val content: String,
)