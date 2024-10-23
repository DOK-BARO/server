package kr.kro.dokbaro.server.core.book.application.port.out

import kr.kro.dokbaro.server.common.dto.option.PageOption
import kr.kro.dokbaro.server.core.book.query.IntegratedBook

interface ReadIntegratedBookCollectionPort {
	fun findAllIntegratedBook(
		pageOption: PageOption,
		keyword: String,
	): Collection<IntegratedBook>
}