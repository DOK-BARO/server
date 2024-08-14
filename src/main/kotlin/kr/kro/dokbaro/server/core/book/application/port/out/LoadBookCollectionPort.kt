package kr.kro.dokbaro.server.core.book.application.port.out

import kr.kro.dokbaro.server.core.book.application.port.out.dto.BookCollectionPagingOption
import kr.kro.dokbaro.server.core.book.application.port.out.dto.LoadBookCollectionCondition
import kr.kro.dokbaro.server.core.book.domain.Book

fun interface LoadBookCollectionPort {
	fun getAll(
		condition: LoadBookCollectionCondition,
		pagingOption: BookCollectionPagingOption,
	): Collection<Book>
}