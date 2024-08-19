package kr.kro.dokbaro.server.core.book.application.port.out.dto

data class BookCollectionPagingOption(
	val offset: Long,
	val limit: Long,
)