package kr.kro.dokbaro.server.core.book.application.port.input.dto

data class FindAllBookCommand(
	val title: String?,
	val authorName: String?,
	val description: String?,
	val category: Long?,
	val lastId: Long?,
	val limit: Long,
)