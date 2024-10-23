package kr.kro.dokbaro.server.core.book.application.port.input.dto

import kr.kro.dokbaro.server.common.dto.option.SortDirection
import kr.kro.dokbaro.server.core.book.query.BookSummarySortOption

data class FindAllBookCommand(
	val title: String? = null,
	val authorName: String? = null,
	val description: String? = null,
	val category: Long? = null,
	val page: Long,
	val size: Long,
	val sort: BookSummarySortOption,
	val direction: SortDirection,
)