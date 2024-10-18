package kr.kro.dokbaro.server.core.bookquiz.application.service

import kr.kro.dokbaro.server.core.bookquiz.application.port.input.FindBookQuizAnswerUseCase
import kr.kro.dokbaro.server.core.bookquiz.application.port.input.FindBookQuizQuestionUseCase
import kr.kro.dokbaro.server.core.bookquiz.application.port.out.ReadBookQuizAnswerPort
import kr.kro.dokbaro.server.core.bookquiz.application.port.out.ReadBookQuizQuestionPort
import kr.kro.dokbaro.server.core.bookquiz.application.service.exception.NotFoundQuestionException
import kr.kro.dokbaro.server.core.bookquiz.application.service.exception.NotFoundQuizException
import kr.kro.dokbaro.server.core.bookquiz.query.BookQuizAnswer
import kr.kro.dokbaro.server.core.bookquiz.query.BookQuizQuestions
import org.springframework.stereotype.Service

@Service
class BookQuizQueryService(
	private val readBookQuizQuestionPort: ReadBookQuizQuestionPort,
	private val readBookQuizAnswerPort: ReadBookQuizAnswerPort,
) : FindBookQuizQuestionUseCase,
	FindBookQuizAnswerUseCase {
	override fun findBookQuizQuestionsBy(quizId: Long): BookQuizQuestions =
		readBookQuizQuestionPort.findBookQuizQuestionsBy(quizId) ?: throw NotFoundQuizException(quizId)

	override fun findBookQuizAnswer(questionId: Long): BookQuizAnswer =
		readBookQuizAnswerPort.findBookQuizAnswerBy(questionId) ?: throw NotFoundQuestionException(questionId)
}