package kr.kro.dokbaro.server.core.book.application.port.input

import kr.kro.dokbaro.server.common.dto.option.PageOption
import kr.kro.dokbaro.server.common.dto.response.PageResponse
import kr.kro.dokbaro.server.core.book.application.port.input.dto.FindAllBookCommand
import kr.kro.dokbaro.server.core.book.query.BookSummary
import kr.kro.dokbaro.server.core.book.query.BookSummarySortKeyword

fun interface FindAllBookUseCase {
	fun findAllBy(
		command: FindAllBookCommand,
		pageOption: PageOption<BookSummarySortKeyword>,
	): PageResponse<BookSummary>
}