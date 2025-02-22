package kr.kro.dokbaro.server.core.bookquiz.application.service

import kr.kro.dokbaro.server.core.bookquiz.application.port.input.FindBookQuizDetailUseCase
import kr.kro.dokbaro.server.core.bookquiz.application.port.out.ReadBookQuizDetailQuestionsPort
import kr.kro.dokbaro.server.core.bookquiz.application.port.out.ReadQuestionElementPort
import kr.kro.dokbaro.server.core.bookquiz.application.port.out.dto.BookQuizDetailQuestions
import kr.kro.dokbaro.server.core.bookquiz.application.service.exception.NotFoundQuizException
import kr.kro.dokbaro.server.core.bookquiz.query.BookQuizDetail
import org.springframework.stereotype.Service

@Service
class BookQuizDetailQueryService(
	private val readBookQuizDetailQuestionsPort: ReadBookQuizDetailQuestionsPort,
	private val readQuestionElementPort: ReadQuestionElementPort,
) : FindBookQuizDetailUseCase {
	override fun findBookQuizDetailBy(id: Long): BookQuizDetail {
		val quizQuestions: BookQuizDetailQuestions =
			readBookQuizDetailQuestionsPort.findBookQuizDetailBy(id) ?: throw NotFoundQuizException(id)

		val questionIds: Collection<Long> = quizQuestions.questions.map { it.id }

		val selectOptions: Map<Long, Collection<String>> = readQuestionElementPort.findSelectOptionBy(questionIds)
		val answerImages: Map<Long, Collection<String>> = readQuestionElementPort.findAnswerExplanationImageBy(questionIds)
		val answers: Map<Long, Collection<String>> = readQuestionElementPort.findAnswersBy(questionIds)

		return BookQuizDetail(
			id = quizQuestions.id,
			title = quizQuestions.title,
			description = quizQuestions.description,
			bookId = quizQuestions.bookId,
			questions =
				quizQuestions.questions.map {
					BookQuizDetail.Question(
						id = it.id,
						content = it.content,
						selectOptions = selectOptions[it.id] ?: emptyList(),
						answerExplanationContent = it.answerExplanationContent,
						answerExplanationImages = answerImages[it.id] ?: emptyList(),
						answerType = it.answerType,
						answers = answers[it.id] ?: emptyList(),
					)
				},
			studyGroupId = quizQuestions.studyGroupId,
			timeLimitSecond = quizQuestions.timeLimitSecond,
			viewScope = quizQuestions.viewScope,
			editScope = quizQuestions.editScope,
			temporary = quizQuestions.temporary,
		)
	}
}