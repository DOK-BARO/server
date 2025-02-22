package kr.kro.dokbaro.server.core.bookquiz.application.port.out

import kr.kro.dokbaro.server.common.dto.option.PageOption
import kr.kro.dokbaro.server.core.bookquiz.query.MyBookQuizSummary
import kr.kro.dokbaro.server.core.bookquiz.query.condition.MyBookQuizSummaryFilterCondition
import kr.kro.dokbaro.server.core.bookquiz.query.sort.MyBookQuizSummarySortKeyword

fun interface ReadMyBookQuizSummaryPort {
	fun findAllMyBookQuiz(
		memberId: Long,
		pageOption: PageOption<MyBookQuizSummarySortKeyword>,
		condition: MyBookQuizSummaryFilterCondition,
	): Collection<MyBookQuizSummary>
}