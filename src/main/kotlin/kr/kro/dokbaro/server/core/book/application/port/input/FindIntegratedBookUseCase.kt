package kr.kro.dokbaro.server.core.book.application.port.input

import kr.kro.dokbaro.server.core.book.query.IntegratedBook

interface FindIntegratedBookUseCase {
	fun findAllIntegratedBooks(
		page: Long,
		size: Long,
		keyword: String,
	): Collection<IntegratedBook>
}