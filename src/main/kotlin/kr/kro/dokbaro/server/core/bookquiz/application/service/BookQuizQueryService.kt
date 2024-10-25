package kr.kro.dokbaro.server.core.bookquiz.application.service

import kr.kro.dokbaro.server.common.dto.option.PageOption
import kr.kro.dokbaro.server.common.dto.option.SortDirection
import kr.kro.dokbaro.server.common.dto.option.SortOption
import kr.kro.dokbaro.server.common.dto.response.PageResponse
import kr.kro.dokbaro.server.core.bookquiz.application.port.input.FindBookQuizAnswerUseCase
import kr.kro.dokbaro.server.core.bookquiz.application.port.input.FindBookQuizQuestionUseCase
import kr.kro.dokbaro.server.core.bookquiz.application.port.input.FindBookQuizSummaryUseCase
import kr.kro.dokbaro.server.core.bookquiz.application.port.out.CountBookQuizPort
import kr.kro.dokbaro.server.core.bookquiz.application.port.out.ReadBookQuizAnswerPort
import kr.kro.dokbaro.server.core.bookquiz.application.port.out.ReadBookQuizQuestionPort
import kr.kro.dokbaro.server.core.bookquiz.application.port.out.ReadBookQuizSummaryPort
import kr.kro.dokbaro.server.core.bookquiz.application.service.exception.NotFoundQuestionException
import kr.kro.dokbaro.server.core.bookquiz.application.service.exception.NotFoundQuizException
import kr.kro.dokbaro.server.core.bookquiz.query.BookQuizAnswer
import kr.kro.dokbaro.server.core.bookquiz.query.BookQuizQuestions
import kr.kro.dokbaro.server.core.bookquiz.query.BookQuizSummary
import kr.kro.dokbaro.server.core.bookquiz.query.BookQuizSummarySortOption
import org.springframework.stereotype.Service

@Service
class BookQuizQueryService(
	private val readBookQuizQuestionPort: ReadBookQuizQuestionPort,
	private val readBookQuizAnswerPort: ReadBookQuizAnswerPort,
	private val countBookQuizPort: CountBookQuizPort,
	private val readBookQuizSummaryPort: ReadBookQuizSummaryPort,
) : FindBookQuizQuestionUseCase,
	FindBookQuizAnswerUseCase,
	FindBookQuizSummaryUseCase {
	override fun findBookQuizQuestionsBy(quizId: Long): BookQuizQuestions =
		readBookQuizQuestionPort.findBookQuizQuestionsBy(quizId) ?: throw NotFoundQuizException(quizId)

	override fun findBookQuizAnswer(questionId: Long): BookQuizAnswer =
		readBookQuizAnswerPort.findBookQuizAnswerBy(questionId) ?: throw NotFoundQuestionException(questionId)

	override fun findAllBookQuizSummary(
		bookId: Long,
		page: Long,
		size: Long,
		sort: BookQuizSummarySortOption,
		direction: SortDirection,
	): PageResponse<BookQuizSummary> {
		val count: Long = countBookQuizPort.countBookQuizBy(bookId)
		val data: Collection<BookQuizSummary> =
			readBookQuizSummaryPort
				.findAllBookQuizSummary(
					bookId,
					PageOption.of(page, size),
					SortOption(sort, direction),
				)

		return PageResponse.of(count, page, data)
	}
}