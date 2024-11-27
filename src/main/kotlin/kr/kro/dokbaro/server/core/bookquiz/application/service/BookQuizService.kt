package kr.kro.dokbaro.server.core.bookquiz.application.service

import kr.kro.dokbaro.server.common.constant.Constants
import kr.kro.dokbaro.server.core.bookquiz.application.port.input.CreateBookQuizUseCase
import kr.kro.dokbaro.server.core.bookquiz.application.port.input.DeleteBookQuizUseCase
import kr.kro.dokbaro.server.core.bookquiz.application.port.input.FindBookQuizByQuestionIdUseCase
import kr.kro.dokbaro.server.core.bookquiz.application.port.input.FindBookQuizUseCase
import kr.kro.dokbaro.server.core.bookquiz.application.port.input.UpdateBookQuizUseCase
import kr.kro.dokbaro.server.core.bookquiz.application.port.input.dto.CreateBookQuizCommand
import kr.kro.dokbaro.server.core.bookquiz.application.port.input.dto.UpdateBookQuizCommand
import kr.kro.dokbaro.server.core.bookquiz.application.port.out.DeleteBookQuizPort
import kr.kro.dokbaro.server.core.bookquiz.application.port.out.InsertBookQuizPort
import kr.kro.dokbaro.server.core.bookquiz.application.port.out.LoadBookQuizByQuestionIdPort
import kr.kro.dokbaro.server.core.bookquiz.application.port.out.LoadBookQuizPort
import kr.kro.dokbaro.server.core.bookquiz.application.port.out.UpdateBookQuizPort
import kr.kro.dokbaro.server.core.bookquiz.application.service.exception.NotFoundQuizException
import kr.kro.dokbaro.server.core.bookquiz.domain.AnswerSheet
import kr.kro.dokbaro.server.core.bookquiz.domain.BookQuiz
import kr.kro.dokbaro.server.core.bookquiz.domain.GradeSheetFactory
import kr.kro.dokbaro.server.core.bookquiz.domain.QuestionAnswer
import kr.kro.dokbaro.server.core.bookquiz.domain.QuizQuestion
import kr.kro.dokbaro.server.core.bookquiz.domain.QuizQuestions
import kr.kro.dokbaro.server.core.bookquiz.domain.SelectOption
import kr.kro.dokbaro.server.core.bookquiz.event.CreatedQuizEvent
import kr.kro.dokbaro.server.core.bookquiz.event.UpdatedQuizEvent
import kr.kro.dokbaro.server.core.member.application.port.input.query.FindCertificatedMemberUseCase
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service

@Service
class BookQuizService(
	private val insertBookQuizPort: InsertBookQuizPort,
	private val findCertificatedMemberUseCase: FindCertificatedMemberUseCase,
	private val loadBookQuizPort: LoadBookQuizPort,
	private val updateBookQuizPort: UpdateBookQuizPort,
	private val loadBookQuizByQuestionIdPort: LoadBookQuizByQuestionIdPort,
	private val deleteBookQuizPort: DeleteBookQuizPort,
	private val eventPublisher: ApplicationEventPublisher,
) : CreateBookQuizUseCase,
	UpdateBookQuizUseCase,
	FindBookQuizUseCase,
	FindBookQuizByQuestionIdUseCase,
	DeleteBookQuizUseCase {
	override fun create(command: CreateBookQuizCommand): Long {
		val loginUser = findCertificatedMemberUseCase.getByCertificationId(command.creatorAuthId)

		val savedId: Long =
			insertBookQuizPort.insert(
				BookQuiz(
					title = command.title,
					description = command.description,
					bookId = command.bookId,
					creatorId = loginUser.id,
					questions =
						QuizQuestions(
							command.questions
								.map {
									QuizQuestion(
										content = it.content,
										selectOptions = it.selectOptions.map { o -> SelectOption(o) },
										answer =
											QuestionAnswer(
												explanationContent = it.answerExplanationContent,
												explanationImages = it.answerExplanationImages,
												gradeSheet = GradeSheetFactory.create(it.answerType, AnswerSheet(it.answers)),
											),
									)
								}.toMutableList(),
						),
					studyGroupId = command.studyGroupId,
				),
			)

		eventPublisher.publishEvent(
			CreatedQuizEvent(
				quizId = savedId,
				creatorId = loginUser.id,
				creatorName = loginUser.nickName,
				studyGroupId = command.studyGroupId,
			),
		)

		return savedId
	}

	override fun update(command: UpdateBookQuizCommand) {
		val bookQuiz: BookQuiz =
			loadBookQuizPort.load(command.id) ?: throw NotFoundQuizException(command.id)

		val modifierId: Long = findCertificatedMemberUseCase.getByCertificationId(command.modifierAuthId).id

		bookQuiz.updateBasicOption(
			title = command.title,
			description = command.description,
			bookId = command.bookId,
			studyGroupId = command.studyGroupId,
			timeLimitSecond = command.timeLimitSecond,
			viewScope = command.viewScope,
			editScope = command.editScope,
		)

		bookQuiz.updateQuestions(
			command.questions.map {
				QuizQuestion(
					id = it.id ?: Constants.UNSAVED_ID,
					content = it.content,
					selectOptions = it.selectOptions.map { o -> SelectOption(o) },
					answer =
						QuestionAnswer(
							explanationContent = it.answerExplanationContent,
							explanationImages = it.answerExplanationImages,
							gradeSheet = GradeSheetFactory.create(it.answerType, AnswerSheet(it.answers)),
						),
				)
			},
			modifierId,
		)

		updateBookQuizPort.update(bookQuiz)

		eventPublisher.publishEvent(
			UpdatedQuizEvent(
				quizId = bookQuiz.id,
				quizTitle = bookQuiz.title,
				quizCreatorId = bookQuiz.creatorId,
			),
		)
	}

	override fun findBy(id: Long): BookQuiz = loadBookQuizPort.load(id) ?: throw NotFoundQuizException(id)

	override fun findByQuestionId(questionId: Long): BookQuiz =
		loadBookQuizByQuestionIdPort.loadByQuestionId(questionId) ?: throw NotFoundQuizException(questionId)

	override fun deleteBy(id: Long) {
		deleteBookQuizPort.deleteBy(id)
	}
}