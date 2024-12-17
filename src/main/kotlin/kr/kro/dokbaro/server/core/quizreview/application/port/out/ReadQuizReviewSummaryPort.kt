package kr.kro.dokbaro.server.core.quizreview.application.port.out

import kr.kro.dokbaro.server.common.dto.option.PageOption
import kr.kro.dokbaro.server.core.quizreview.application.port.out.dto.ReadQuizReviewSummaryCondition
import kr.kro.dokbaro.server.core.quizreview.query.QuizReviewSummary
import kr.kro.dokbaro.server.core.quizreview.query.QuizReviewSummarySortKeyword

fun interface ReadQuizReviewSummaryPort {
	fun findAllQuizReviewSummaryBy(
		condition: ReadQuizReviewSummaryCondition,
		pageOption: PageOption<QuizReviewSummarySortKeyword>,
	): Collection<QuizReviewSummary>
}