package kr.kro.dokbaro.server.core.book.application.port.out

import kr.kro.dokbaro.server.core.book.query.IntegratedBook

fun interface ReadIntegratedBookCollectionPort {
	fun findAllIntegratedBook(
		size: Long,
		keyword: String,
		lastId: Long?,
	): Collection<IntegratedBook>
}