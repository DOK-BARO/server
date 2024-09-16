package kr.kro.dokbaro.server.core.book.application.port.input.dto

import java.time.LocalDate

data class CreateBookCommand(
	val isbn: String,
	val title: String,
	val publisher: String,
	val publishedAt: LocalDate,
	val price: Int,
	val description: String?,
	val imageUrl: String?,
	val categories: Set<Long>,
	val authors: List<String>,
)