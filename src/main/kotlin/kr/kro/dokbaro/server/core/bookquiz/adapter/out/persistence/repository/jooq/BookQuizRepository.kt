package kr.kro.dokbaro.server.core.bookquiz.adapter.out.persistence.repository.jooq

import kr.kro.dokbaro.server.core.bookquiz.domain.BookQuiz
import org.jooq.DSLContext
import org.jooq.generated.tables.JBookQuiz
import org.springframework.stereotype.Repository

@Repository
class BookQuizRepository(
	private val dslContext: DSLContext,
) {
	companion object {
		private val BOOK_QUIZ = JBookQuiz.BOOK_QUIZ
	}

	fun insert(bookQuiz: BookQuiz): Long =
		dslContext
			.insertInto(
				BOOK_QUIZ,
				BOOK_QUIZ.TITLE,
				BOOK_QUIZ.DESCRIPTION,
				BOOK_QUIZ.CREATOR_ID,
				BOOK_QUIZ.BOOK_ID,
			).values(
				bookQuiz.title,
				bookQuiz.description.toByteArray(),
				bookQuiz.creatorId,
				bookQuiz.bookId,
			).returningResult(BOOK_QUIZ.ID)
			.fetchOneInto(Long::class.java)!!
}