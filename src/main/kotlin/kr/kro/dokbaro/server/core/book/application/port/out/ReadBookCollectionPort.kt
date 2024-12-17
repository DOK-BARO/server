package kr.kro.dokbaro.server.core.book.application.port.out

import kr.kro.dokbaro.server.common.dto.option.PageOption
import kr.kro.dokbaro.server.core.book.application.port.out.dto.ReadBookCollectionCondition
import kr.kro.dokbaro.server.core.book.query.BookSummary
import kr.kro.dokbaro.server.core.book.query.BookSummarySortKeyword

fun interface ReadBookCollectionPort {
	fun getAllBook(
		condition: ReadBookCollectionCondition,
		pageOption: PageOption<BookSummarySortKeyword>,
	): Collection<BookSummary>
}