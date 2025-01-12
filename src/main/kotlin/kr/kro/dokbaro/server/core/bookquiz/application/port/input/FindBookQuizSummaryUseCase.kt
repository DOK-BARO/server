package kr.kro.dokbaro.server.core.bookquiz.application.port.input

import kr.kro.dokbaro.server.common.dto.option.PageOption
import kr.kro.dokbaro.server.common.dto.response.PageResponse
import kr.kro.dokbaro.server.core.bookquiz.application.port.input.dto.FindAllBookQuizSummaryCommand
import kr.kro.dokbaro.server.core.bookquiz.query.BookQuizSummary
import kr.kro.dokbaro.server.core.bookquiz.query.sort.BookQuizSummarySortKeyword

fun interface FindBookQuizSummaryUseCase {
	fun findAllBookQuizSummary(
		command: FindAllBookQuizSummaryCommand,
		pageOption: PageOption<BookQuizSummarySortKeyword>,
	): PageResponse<BookQuizSummary>
}