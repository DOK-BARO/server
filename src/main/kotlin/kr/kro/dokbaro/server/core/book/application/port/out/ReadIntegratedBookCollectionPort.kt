package kr.kro.dokbaro.server.core.book.application.port.out

import kr.kro.dokbaro.server.common.dto.page.PagingOption
import kr.kro.dokbaro.server.core.book.query.BookSummary

interface ReadIntegratedBookCollectionPort {
	fun findAllIntegratedBook(
		pagingOption: PagingOption,
		keyword: String,
	): Collection<BookSummary>
}