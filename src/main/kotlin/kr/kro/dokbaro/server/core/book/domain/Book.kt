package kr.kro.dokbaro.server.core.book.domain

import kr.kro.dokbaro.server.common.constant.Constants
import java.time.LocalDate

data class Book(
	val id: Long = Constants.UNSAVED_ID,
	val isbn: String,
	val title: String,
	val publisher: String,
	val publishedAt: LocalDate,
	val price: Int,
	val description: String?,
	val imageUrl: String?,
	val categories: Set<Long>,
	val authors: Collection<BookAuthor>,
)