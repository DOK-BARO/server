package kr.kro.dokbaro.server.core.book.adapter.input.web.dto

import kr.kro.dokbaro.server.core.book.domain.Book
import java.time.LocalDate

data class BookSummary(
	val id: Long,
	val isbn: String,
	val title: String,
	val publisher: String,
	val publishedAt: LocalDate,
	val imageUrl: String?,
	val categories: List<BookCategoryResponse>,
	val authors: List<String>,
) {
	constructor(
		book: Book,
	) : this(
		book.id,
		book.isbn,
		book.title,
		book.publisher,
		book.publishedAt,
		book.imageUrl,
		book.categories
			.map {
				BookCategoryResponse(it.id, it.name)
			},
		book.authors.map { it.name },
	)
}