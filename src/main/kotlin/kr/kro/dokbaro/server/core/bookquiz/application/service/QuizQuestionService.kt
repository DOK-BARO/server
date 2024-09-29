package kr.kro.dokbaro.server.core.bookquiz.application.service

import kr.kro.dokbaro.server.core.bookquiz.application.port.input.CreateQuizQuestionUseCase
import kr.kro.dokbaro.server.core.bookquiz.application.port.input.dto.CreateQuizQuestionCommand
import kr.kro.dokbaro.server.core.bookquiz.application.port.out.InsertQuizQuestionPort
import kr.kro.dokbaro.server.core.bookquiz.domain.AnswerFactory
import kr.kro.dokbaro.server.core.bookquiz.domain.AnswerSheet
import kr.kro.dokbaro.server.core.bookquiz.domain.QuizQuestion
import kr.kro.dokbaro.server.core.bookquiz.domain.SelectOption
import org.springframework.stereotype.Service

@Service
class QuizQuestionService(
	private val insertQuizQuestionPort: InsertQuizQuestionPort,
) : CreateQuizQuestionUseCase {
	override fun create(command: CreateQuizQuestionCommand): Long {
		val answer = AnswerFactory.create(command.answerType, AnswerSheet(command.answers))
		val selectOptions = command.selectOptions.map { SelectOption(it) }

		return insertQuizQuestionPort.insert(
			QuizQuestion(
				content = command.content,
				selectOptions = selectOptions,
				answerExplanation = command.answerExplanation,
				answer = answer,
				quizId = command.quizId,
			),
		)
	}
}