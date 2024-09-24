package kr.kro.dokbaro.server.core.book.application.port.input.dto

data class FindAllBookCommand(
	val title: String? = null,
	val authorName: String? = null,
	val description: String? = null,
	val category: Long? = null,
	val page: Long,
	val size: Long,
)