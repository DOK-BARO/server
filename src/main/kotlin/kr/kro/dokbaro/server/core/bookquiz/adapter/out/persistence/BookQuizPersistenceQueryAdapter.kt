package kr.kro.dokbaro.server.core.bookquiz.adapter.out.persistence

import kr.kro.dokbaro.server.common.annotation.PersistenceAdapter
import kr.kro.dokbaro.server.common.dto.option.PageOption
import kr.kro.dokbaro.server.common.dto.option.SortOption
import kr.kro.dokbaro.server.core.bookquiz.adapter.out.persistence.repository.jooq.BookQuizQueryRepository
import kr.kro.dokbaro.server.core.bookquiz.application.port.out.CountBookQuizPort
import kr.kro.dokbaro.server.core.bookquiz.application.port.out.ReadBookQuizAnswerPort
import kr.kro.dokbaro.server.core.bookquiz.application.port.out.ReadBookQuizQuestionPort
import kr.kro.dokbaro.server.core.bookquiz.application.port.out.ReadBookQuizSummaryPort
import kr.kro.dokbaro.server.core.bookquiz.query.BookQuizAnswer
import kr.kro.dokbaro.server.core.bookquiz.query.BookQuizQuestions
import kr.kro.dokbaro.server.core.bookquiz.query.BookQuizSummary
import kr.kro.dokbaro.server.core.bookquiz.query.BookQuizSummarySortOption

@PersistenceAdapter
class BookQuizPersistenceQueryAdapter(
	private val bookQuizQueryRepository: BookQuizQueryRepository,
) : ReadBookQuizQuestionPort,
	ReadBookQuizAnswerPort,
	CountBookQuizPort,
	ReadBookQuizSummaryPort {
	override fun findBookQuizQuestionsBy(quizId: Long): BookQuizQuestions? =
		bookQuizQueryRepository.findBookQuizQuestionsBy(quizId)

	override fun findBookQuizAnswerBy(questionId: Long): BookQuizAnswer? =
		bookQuizQueryRepository.findBookQuizAnswerBy(questionId)

	override fun countBookQuizBy(bookId: Long): Long = bookQuizQueryRepository.countBookQuizBy(bookId)

	override fun findAllBookQuizSummary(
		bookId: Long,
		pageOption: PageOption,
		sortOption: SortOption<BookQuizSummarySortOption>,
	): Collection<BookQuizSummary> = bookQuizQueryRepository.findAllBookQuizSummary(bookId, pageOption, sortOption)
}