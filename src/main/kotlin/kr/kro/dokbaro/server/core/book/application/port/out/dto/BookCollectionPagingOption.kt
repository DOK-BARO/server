package kr.kro.dokbaro.server.core.book.application.port.out.dto

data class BookCollectionPagingOption(
	val lastId: Long?,
	val limit: Long,
)