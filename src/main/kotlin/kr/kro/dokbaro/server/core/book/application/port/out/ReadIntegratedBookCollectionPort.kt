package kr.kro.dokbaro.server.core.book.application.port.out

import kr.kro.dokbaro.server.common.dto.option.PageOption
import kr.kro.dokbaro.server.core.book.query.BookSummary

interface ReadIntegratedBookCollectionPort {
	fun findAllIntegratedBook(
		pageOption: PageOption,
		keyword: String,
	): Collection<BookSummary>
}