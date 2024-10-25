package kr.kro.dokbaro.server.core.bookquiz.application.port.out

import kr.kro.dokbaro.server.common.dto.option.PageOption
import kr.kro.dokbaro.server.common.dto.option.SortOption
import kr.kro.dokbaro.server.core.bookquiz.query.BookQuizSummary
import kr.kro.dokbaro.server.core.bookquiz.query.BookQuizSummarySortOption

fun interface ReadBookQuizSummaryPort {
	fun findAllBookQuizSummary(
		bookId: Long,
		pageOption: PageOption,
		sortOption: SortOption<BookQuizSummarySortOption>,
	): Collection<BookQuizSummary>
}