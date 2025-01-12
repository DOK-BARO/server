package kr.kro.dokbaro.server.core.bookquiz.application.service

import kr.kro.dokbaro.server.common.dto.option.PageOption
import kr.kro.dokbaro.server.common.dto.response.PageResponse
import kr.kro.dokbaro.server.core.bookquiz.application.port.input.FindBookQuizAnswerUseCase
import kr.kro.dokbaro.server.core.bookquiz.application.port.input.FindBookQuizExplanationUseCase
import kr.kro.dokbaro.server.core.bookquiz.application.port.input.FindBookQuizQuestionUseCase
import kr.kro.dokbaro.server.core.bookquiz.application.port.input.FindBookQuizSummaryUseCase
import kr.kro.dokbaro.server.core.bookquiz.application.port.input.FindMyBookQuizUseCase
import kr.kro.dokbaro.server.core.bookquiz.application.port.input.FindUnsolvedGroupBookQuizUseCase
import kr.kro.dokbaro.server.core.bookquiz.application.port.input.dto.FindAllBookQuizSummaryCommand
import kr.kro.dokbaro.server.core.bookquiz.application.port.out.CountBookQuizPort
import kr.kro.dokbaro.server.core.bookquiz.application.port.out.ReadBookQuizAnswerPort
import kr.kro.dokbaro.server.core.bookquiz.application.port.out.ReadBookQuizExplanationPort
import kr.kro.dokbaro.server.core.bookquiz.application.port.out.ReadBookQuizQuestionPort
import kr.kro.dokbaro.server.core.bookquiz.application.port.out.ReadBookQuizSummaryPort
import kr.kro.dokbaro.server.core.bookquiz.application.port.out.ReadMyBookQuizSummaryPort
import kr.kro.dokbaro.server.core.bookquiz.application.port.out.ReadUnsolvedGroupBookQuizPort
import kr.kro.dokbaro.server.core.bookquiz.application.port.out.dto.CountBookQuizCondition
import kr.kro.dokbaro.server.core.bookquiz.application.port.out.dto.FindBookQuizCondition
import kr.kro.dokbaro.server.core.bookquiz.application.service.exception.NotFoundQuizException
import kr.kro.dokbaro.server.core.bookquiz.domain.exception.NotFoundQuestionException
import kr.kro.dokbaro.server.core.bookquiz.query.BookQuizAnswer
import kr.kro.dokbaro.server.core.bookquiz.query.BookQuizExplanation
import kr.kro.dokbaro.server.core.bookquiz.query.BookQuizQuestions
import kr.kro.dokbaro.server.core.bookquiz.query.BookQuizSummary
import kr.kro.dokbaro.server.core.bookquiz.query.MyBookQuizSummary
import kr.kro.dokbaro.server.core.bookquiz.query.UnsolvedGroupBookQuizSummary
import kr.kro.dokbaro.server.core.bookquiz.query.sort.BookQuizSummarySortKeyword
import kr.kro.dokbaro.server.core.bookquiz.query.sort.MyBookQuizSummarySortKeyword
import kr.kro.dokbaro.server.core.bookquiz.query.sort.UnsolvedGroupBookQuizSortKeyword
import org.springframework.stereotype.Service

@Service
class BookQuizQueryService(
	private val readBookQuizQuestionPort: ReadBookQuizQuestionPort,
	private val readBookQuizAnswerPort: ReadBookQuizAnswerPort,
	private val countBookQuizPort: CountBookQuizPort,
	private val readBookQuizSummaryPort: ReadBookQuizSummaryPort,
	private val findUnsolvedGroupBookQuizPort: ReadUnsolvedGroupBookQuizPort,
	private val readMyBookQuizSummaryPort: ReadMyBookQuizSummaryPort,
	private val readBookQuizExplanationPort: ReadBookQuizExplanationPort,
) : FindBookQuizQuestionUseCase,
	FindBookQuizAnswerUseCase,
	FindBookQuizSummaryUseCase,
	FindUnsolvedGroupBookQuizUseCase,
	FindMyBookQuizUseCase,
	FindBookQuizExplanationUseCase {
	override fun findBookQuizQuestionsBy(quizId: Long): BookQuizQuestions =
		readBookQuizQuestionPort.findBookQuizQuestionsBy(quizId) ?: throw NotFoundQuizException(quizId)

	override fun findBookQuizAnswer(questionId: Long): BookQuizAnswer =
		readBookQuizAnswerPort.findBookQuizAnswerBy(questionId) ?: throw NotFoundQuestionException(questionId)

	override fun findAllBookQuizSummary(
		command: FindAllBookQuizSummaryCommand,
		pageOption: PageOption<BookQuizSummarySortKeyword>,
	): PageResponse<BookQuizSummary> {
		val count: Long =
			countBookQuizPort.countBookQuizBy(
				CountBookQuizCondition(bookId = command.bookId),
			)
		val data: Collection<BookQuizSummary> =
			readBookQuizSummaryPort
				.findAllBookQuizSummary(
					condition =
						FindBookQuizCondition(
							bookId = command.bookId,
							studyGroup =
								FindBookQuizCondition.StudyGroup(
									all = command.studyGroup.all,
									id = command.studyGroup.id,
								),
						),
					pageOption = pageOption,
				)

		return PageResponse.of(
			totalElementCount = count,
			pageSize = pageOption.size,
			data = data,
		)
	}

	override fun findAllUnsolvedQuizzes(
		memberId: Long,
		studyGroupId: Long,
		pageOption: PageOption<UnsolvedGroupBookQuizSortKeyword>,
	): PageResponse<UnsolvedGroupBookQuizSummary> {
		val totalCount: Long =
			countBookQuizPort.countBookQuizBy(
				CountBookQuizCondition(
					studyGroup = CountBookQuizCondition.StudyGroup(all = false, id = studyGroupId),
					solved =
						CountBookQuizCondition.Solved(
							memberId = memberId,
							solved = false,
						),
				),
			)

		val data: Collection<UnsolvedGroupBookQuizSummary> =
			findUnsolvedGroupBookQuizPort.findAllUnsolvedQuizzes(
				memberId = memberId,
				studyGroupId = studyGroupId,
				pageOption = pageOption,
			)

		return PageResponse.of(
			totalElementCount = totalCount,
			pageSize = pageOption.size,
			data = data,
		)
	}

	override fun findMyBookQuiz(
		memberId: Long,
		pageOption: PageOption<MyBookQuizSummarySortKeyword>,
	): PageResponse<MyBookQuizSummary> {
		val totalCount: Long = countBookQuizPort.countBookQuizBy(CountBookQuizCondition(creatorId = memberId))

		val data: Collection<MyBookQuizSummary> =
			readMyBookQuizSummaryPort.findAllMyBookQuiz(
				memberId = memberId,
				pageOption = pageOption,
			)

		return PageResponse.of(
			totalElementCount = totalCount,
			pageSize = pageOption.size,
			data = data,
		)
	}

	override fun findExplanationBy(id: Long): BookQuizExplanation =
		readBookQuizExplanationPort.findExplanationBy(id) ?: throw NotFoundQuizException(id)
}