package kr.kro.dokbaro.server.core.book.application.port.out

import kr.kro.dokbaro.server.common.dto.page.PagingOption
import kr.kro.dokbaro.server.core.book.application.port.out.dto.ReadBookCollectionCondition
import kr.kro.dokbaro.server.core.book.query.BookSummary

fun interface ReadBookCollectionPort {
	fun getAllBook(
		condition: ReadBookCollectionCondition,
		pagingOption: PagingOption,
	): Collection<BookSummary>
}