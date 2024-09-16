package kr.kro.dokbaro.server.core.book.domain

import java.time.LocalDate

data class Book(
	val isbn: String,
	val title: String,
	val publisher: String,
	val publishedAt: LocalDate,
	val price: Int,
	val description: String?,
	val imageUrl: String?,
	val categories: Set<BookCategory>,
	val authors: List<BookAuthor>,
	val id: Long = Constants.UNSAVED_ID,
)