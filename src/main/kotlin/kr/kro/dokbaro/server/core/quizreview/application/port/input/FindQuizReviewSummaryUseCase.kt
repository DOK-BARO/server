package kr.kro.dokbaro.server.core.quizreview.application.port.input

import kr.kro.dokbaro.server.common.dto.response.PageResponse
import kr.kro.dokbaro.server.core.quizreview.application.port.input.dto.FindQuizReviewSummaryCommand
import kr.kro.dokbaro.server.core.quizreview.query.QuizReviewSummary

fun interface FindQuizReviewSummaryUseCase {
	fun findAllQuizReviewSummaryBy(command: FindQuizReviewSummaryCommand): PageResponse<QuizReviewSummary>
}