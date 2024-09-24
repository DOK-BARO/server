package kr.kro.dokbaro.server.core.book.application.port.input.dto

data class CreateBookCategoryCommand(
	val koreanName: String,
	val englishName: String,
	val parentId: Long,
)