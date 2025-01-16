package kr.kro.dokbaro.server.core.bookquiz.adapter.out.persistence

import kr.kro.dokbaro.server.common.annotation.PersistenceAdapter
import kr.kro.dokbaro.server.common.dto.option.PageOption
import kr.kro.dokbaro.server.core.bookquiz.adapter.out.persistence.repository.jooq.BookQuizQueryRepository
import kr.kro.dokbaro.server.core.bookquiz.application.port.out.CountBookQuizPort
import kr.kro.dokbaro.server.core.bookquiz.application.port.out.ReadBookQuizAnswerPort
import kr.kro.dokbaro.server.core.bookquiz.application.port.out.ReadBookQuizDetailQuestionsPort
import kr.kro.dokbaro.server.core.bookquiz.application.port.out.ReadBookQuizExplanationPort
import kr.kro.dokbaro.server.core.bookquiz.application.port.out.ReadBookQuizQuestionPort
import kr.kro.dokbaro.server.core.bookquiz.application.port.out.ReadBookQuizSummaryPort
import kr.kro.dokbaro.server.core.bookquiz.application.port.out.ReadMyBookQuizSummaryPort
import kr.kro.dokbaro.server.core.bookquiz.application.port.out.ReadQuestionElementPort
import kr.kro.dokbaro.server.core.bookquiz.application.port.out.ReadUnsolvedGroupBookQuizPort
import kr.kro.dokbaro.server.core.bookquiz.application.port.out.dto.BookQuizDetailQuestions
import kr.kro.dokbaro.server.core.bookquiz.application.port.out.dto.CountBookQuizCondition
import kr.kro.dokbaro.server.core.bookquiz.query.BookQuizAnswer
import kr.kro.dokbaro.server.core.bookquiz.query.BookQuizExplanation
import kr.kro.dokbaro.server.core.bookquiz.query.BookQuizQuestions
import kr.kro.dokbaro.server.core.bookquiz.query.BookQuizSummary
import kr.kro.dokbaro.server.core.bookquiz.query.MyBookQuizSummary
import kr.kro.dokbaro.server.core.bookquiz.query.UnsolvedGroupBookQuizSummary
import kr.kro.dokbaro.server.core.bookquiz.query.sort.BookQuizSummarySortKeyword
import kr.kro.dokbaro.server.core.bookquiz.query.sort.MyBookQuizSummarySortKeyword
import kr.kro.dokbaro.server.core.bookquiz.query.sort.UnsolvedGroupBookQuizSortKeyword

@PersistenceAdapter
class BookQuizPersistenceQueryAdapter(
	private val bookQuizQueryRepository: BookQuizQueryRepository,
) : ReadBookQuizQuestionPort,
	ReadBookQuizAnswerPort,
	CountBookQuizPort,
	ReadBookQuizSummaryPort,
	ReadUnsolvedGroupBookQuizPort,
	ReadMyBookQuizSummaryPort,
	ReadBookQuizExplanationPort,
	ReadBookQuizDetailQuestionsPort,
	ReadQuestionElementPort {
	override fun findBookQuizQuestionsBy(quizId: Long): BookQuizQuestions? =
		bookQuizQueryRepository.findBookQuizQuestionsBy(quizId)

	override fun findBookQuizAnswerBy(questionId: Long): BookQuizAnswer? =
		bookQuizQueryRepository.findBookQuizAnswerBy(questionId)

	override fun countBookQuizBy(condition: CountBookQuizCondition): Long =
		bookQuizQueryRepository.countBookQuizBy(condition)

	override fun findAllBookQuizSummary(
		bookId: Long,
		pageOption: PageOption<BookQuizSummarySortKeyword>,
	): Collection<BookQuizSummary> =
		bookQuizQueryRepository.findAllBookQuizSummary(
			bookId = bookId,
			pageOption = pageOption,
		)

	override fun findAllUnsolvedQuizzes(
		memberId: Long,
		studyGroupId: Long,
		pageOption: PageOption<UnsolvedGroupBookQuizSortKeyword>,
	): Collection<UnsolvedGroupBookQuizSummary> =
		bookQuizQueryRepository.findAllUnsolvedQuizzes(
			memberId = memberId,
			studyGroupId = studyGroupId,
			pageOption = pageOption,
		)

	override fun findAllMyBookQuiz(
		memberId: Long,
		pageOption: PageOption<MyBookQuizSummarySortKeyword>,
	): Collection<MyBookQuizSummary> = bookQuizQueryRepository.findAllMyBookQuizzes(memberId, pageOption)

	override fun findExplanationBy(id: Long): BookQuizExplanation? = bookQuizQueryRepository.findBookQuizExplanationBy(id)

	override fun findBookQuizDetailBy(id: Long): BookQuizDetailQuestions? =
		bookQuizQueryRepository.findBookQuizDetailBy(id)

	override fun findSelectOptionBy(ids: Collection<Long>): Map<Long, Collection<String>> =
		bookQuizQueryRepository.findSelectOptionBy(ids)

	override fun findAnswerExplanationImageBy(ids: Collection<Long>): Map<Long, Collection<String>> =
		bookQuizQueryRepository.findAnswerExplanationImageBy(ids)

	override fun findAnswersBy(ids: Collection<Long>): Map<Long, Collection<String>> =
		bookQuizQueryRepository.findAnswerBy(ids)
}