package kr.kro.dokbaro.server.fixture.domain

import kr.kro.dokbaro.server.common.constant.Constants
import kr.kro.dokbaro.server.core.book.domain.Book
import kr.kro.dokbaro.server.core.book.domain.BookAuthor
import kr.kro.dokbaro.server.core.book.domain.BookCategory
import kr.kro.dokbaro.server.core.book.query.BookCategorySingle
import kr.kro.dokbaro.server.core.book.query.BookDetail
import kr.kro.dokbaro.server.core.book.query.BookSummary
import java.time.LocalDate
import kotlin.random.Random

fun bookFixture(
	isbn: String = Random.nextLong(10000000000).toString(),
	title: String = "Effective Kotlin",
	publisher: String = "Manning",
	publishedAt: LocalDate = LocalDate.of(2020, 1, 1),
	price: Int = 45000,
	description: String? = "A comprehensive guide to writing Kotlin code.",
	imageUrl: String? = "https://example.com/effective_kotlin.jpg",
	categories: Set<Long> = setOf(BookCategory.ROOT_ID),
	authors: Collection<String> = listOf("조영호"),
	id: Long = Constants.UNSAVED_ID,
): Book =
	Book(
		isbn,
		title,
		publisher,
		publishedAt,
		price,
		description,
		imageUrl,
		categories,
		authors.map { BookAuthor(it) },
		id,
	)

fun bookCategoryFixture(
	koreanName: String = "ROOT",
	englishName: String = "ROOT",
	parentId: Long? = null,
	id: Long = BookCategory.ROOT_ID,
): BookCategory =
	BookCategory(
		koreanName,
		englishName,
		parentId,
		id,
	)

fun bookDetailFixture(
	id: Long = 1,
	isbn: String = "1234567891234",
	title: String = "점프투자바",
	publisher: String = "위키북스",
	description: String = "이책 진짜 좋아요",
	imageUrl: String = "image.png",
	categories: List<BookCategorySingle> =
		listOf(
			BookCategorySingle(
				7,
				"TCP",
				BookCategorySingle(
					4,
					"네트워크",
					BookCategorySingle(2, "IT", null),
				),
			),
			BookCategorySingle(5, "개발방법론", BookCategorySingle(2, "IT", null)),
		),
	authors: List<String> = listOf("마틴 파울러", "조영호"),
): BookDetail =
	BookDetail(
		id = id,
		isbn = isbn,
		title = title,
		publisher = publisher,
		description = description,
		imageUrl = imageUrl,
		categories = categories,
		authors = authors,
	)

fun bookSummaryFixture(
	id: Long = 1,
	title: String = "점프투자바",
	publisher: String = "위키북스",
	imageUrl: String = "image.png",
	authors: List<String> = listOf("마틴 파울러", "조영호"),
): BookSummary =
	BookSummary(
		id = id,
		title = title,
		publisher = publisher,
		imageUrl = imageUrl,
		authors = authors,
	)