package kr.kro.dokbaro.server.core.quizreview.application.port.input

import kr.kro.dokbaro.server.common.dto.option.PageOption
import kr.kro.dokbaro.server.common.dto.response.PageResponse
import kr.kro.dokbaro.server.core.quizreview.application.port.input.dto.FindQuizReviewSummaryCommand
import kr.kro.dokbaro.server.core.quizreview.query.QuizReviewSummary
import kr.kro.dokbaro.server.core.quizreview.query.QuizReviewSummarySortKeyword

fun interface FindQuizReviewSummaryUseCase {
	fun findAllQuizReviewSummaryBy(
		command: FindQuizReviewSummaryCommand,
		pageOption: PageOption<QuizReviewSummarySortKeyword>,
	): PageResponse<QuizReviewSummary>
}