package kr.kro.dokbaro.server.core.quizreview.application.port.input.dto

import kr.kro.dokbaro.server.common.dto.option.SortDirection
import kr.kro.dokbaro.server.core.quizreview.query.QuizReviewSummarySortOption

data class FindQuizReviewSummaryCommand(
	val page: Long,
	val size: Long,
	val quizId: Long,
	val sort: QuizReviewSummarySortOption,
	val direction: SortDirection,
)