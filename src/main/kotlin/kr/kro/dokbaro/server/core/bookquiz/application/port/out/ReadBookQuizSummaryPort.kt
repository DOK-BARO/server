package kr.kro.dokbaro.server.core.bookquiz.application.port.out

import kr.kro.dokbaro.server.common.dto.option.PageOption
import kr.kro.dokbaro.server.core.bookquiz.application.port.out.dto.FindBookQuizCondition
import kr.kro.dokbaro.server.core.bookquiz.query.BookQuizSummary
import kr.kro.dokbaro.server.core.bookquiz.query.sort.BookQuizSummarySortKeyword

fun interface ReadBookQuizSummaryPort {
	fun findAllBookQuizSummary(
		condition: FindBookQuizCondition,
		pageOption: PageOption<BookQuizSummarySortKeyword>,
	): Collection<BookQuizSummary>
}