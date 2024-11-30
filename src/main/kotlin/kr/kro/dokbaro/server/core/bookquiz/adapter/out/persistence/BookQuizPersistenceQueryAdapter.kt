package kr.kro.dokbaro.server.core.bookquiz.adapter.out.persistence

import kr.kro.dokbaro.server.common.annotation.PersistenceAdapter
import kr.kro.dokbaro.server.common.dto.option.PageOption
import kr.kro.dokbaro.server.common.dto.option.SortOption
import kr.kro.dokbaro.server.core.bookquiz.adapter.out.persistence.repository.jooq.BookQuizQueryRepository
import kr.kro.dokbaro.server.core.bookquiz.application.port.out.CountBookQuizPort
import kr.kro.dokbaro.server.core.bookquiz.application.port.out.ReadBookQuizAnswerPort
import kr.kro.dokbaro.server.core.bookquiz.application.port.out.ReadBookQuizExplanationPort
import kr.kro.dokbaro.server.core.bookquiz.application.port.out.ReadBookQuizQuestionPort
import kr.kro.dokbaro.server.core.bookquiz.application.port.out.ReadBookQuizSummaryPort
import kr.kro.dokbaro.server.core.bookquiz.application.port.out.ReadMyBookQuizSummaryPort
import kr.kro.dokbaro.server.core.bookquiz.application.port.out.ReadUnsolvedGroupBookQuizPort
import kr.kro.dokbaro.server.core.bookquiz.query.BookQuizAnswer
import kr.kro.dokbaro.server.core.bookquiz.query.BookQuizExplanation
import kr.kro.dokbaro.server.core.bookquiz.query.BookQuizQuestions
import kr.kro.dokbaro.server.core.bookquiz.query.BookQuizSummary
import kr.kro.dokbaro.server.core.bookquiz.query.BookQuizSummarySortOption
import kr.kro.dokbaro.server.core.bookquiz.query.MyBookQuizSummary
import kr.kro.dokbaro.server.core.bookquiz.query.UnsolvedGroupBookQuizSummary

@PersistenceAdapter
class BookQuizPersistenceQueryAdapter(
	private val bookQuizQueryRepository: BookQuizQueryRepository,
) : ReadBookQuizQuestionPort,
	ReadBookQuizAnswerPort,
	CountBookQuizPort,
	ReadBookQuizSummaryPort,
	ReadUnsolvedGroupBookQuizPort,
	ReadMyBookQuizSummaryPort,
	ReadBookQuizExplanationPort {
	override fun findBookQuizQuestionsBy(quizId: Long): BookQuizQuestions? =
		bookQuizQueryRepository.findBookQuizQuestionsBy(quizId)

	override fun findBookQuizAnswerBy(questionId: Long): BookQuizAnswer? =
		bookQuizQueryRepository.findBookQuizAnswerBy(questionId)

	override fun countBookQuizBy(bookId: Long): Long = bookQuizQueryRepository.countBookQuizBy(bookId)

	override fun findAllBookQuizSummary(
		bookId: Long,
		pageOption: PageOption,
		sortOption: SortOption<BookQuizSummarySortOption>,
	): Collection<BookQuizSummary> =
		bookQuizQueryRepository.findAllBookQuizSummary(
			bookId = bookId,
			pageOption = pageOption,
			sortOption = sortOption,
		)

	override fun findAllUnsolvedQuizzes(
		memberId: Long,
		studyGroupId: Long,
	): Collection<UnsolvedGroupBookQuizSummary> =
		bookQuizQueryRepository.findAllUnsolvedQuizzes(
			memberId = memberId,
			studyGroupId = studyGroupId,
		)

	override fun findAllMyBookQuiz(memberId: Long): Collection<MyBookQuizSummary> =
		bookQuizQueryRepository.findAllMyBookQuizzes(memberId)

	override fun findExplanationBy(id: Long): BookQuizExplanation? = bookQuizQueryRepository.findBookQuizExplanationBy(id)
}