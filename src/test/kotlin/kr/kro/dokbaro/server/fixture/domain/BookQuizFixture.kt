package kr.kro.dokbaro.server.fixture.domain

import kr.kro.dokbaro.server.core.bookquiz.domain.BookQuiz

fun bookQuizFixture(
	title: String = "title",
	description: String = "description",
	bookId: Long = 0,
	creatorMemberId: Long = 0,
	id: Long = 0,
) = BookQuiz(
	title = title,
	description = description,
	bookId = bookId,
	creatorMemberId = creatorMemberId,
	id = id,
)