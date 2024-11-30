package kr.kro.dokbaro.server.core.book.application.port.input

import kr.kro.dokbaro.server.core.book.query.IntegratedBook

fun interface FindIntegratedBookUseCase {
	fun findAllIntegratedBooks(
		size: Long,
		keyword: String,
		lastId: Long?,
	): Collection<IntegratedBook>
}