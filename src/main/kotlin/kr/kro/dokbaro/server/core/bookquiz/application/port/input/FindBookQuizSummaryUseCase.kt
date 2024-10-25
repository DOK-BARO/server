package kr.kro.dokbaro.server.core.bookquiz.application.port.input

import kr.kro.dokbaro.server.common.dto.option.SortDirection
import kr.kro.dokbaro.server.common.dto.response.PageResponse
import kr.kro.dokbaro.server.core.bookquiz.query.BookQuizSummary
import kr.kro.dokbaro.server.core.bookquiz.query.BookQuizSummarySortOption

fun interface FindBookQuizSummaryUseCase {
	fun findAllBookQuizSummary(
		bookId: Long,
		page: Long,
		size: Long,
		sort: BookQuizSummarySortOption,
		direction: SortDirection,
	): PageResponse<BookQuizSummary>
}