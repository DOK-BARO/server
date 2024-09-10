package kr.kro.dokbaro.server.core.bookquiz.adapter.input.web.dto

data class CreateBookQuizRequest(
	val title: String,
	val description: String,
	val bookId: Long,
)