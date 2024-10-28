package kr.kro.dokbaro.server.core.bookquiz.domain

import kr.kro.dokbaro.server.common.constant.Constants
import kr.kro.dokbaro.server.core.bookquiz.domain.exception.IllegalSubmitSheetFormatException
import kr.kro.dokbaro.server.core.bookquiz.domain.exception.NotFoundQuestionException
import kr.kro.dokbaro.server.core.bookquiz.domain.exception.QuizTypeMissMatchException

data class QuizQuestions(
	private val questions: MutableList<QuizQuestion> = mutableListOf(),
) {
	fun getQuestions(): List<QuizQuestion> = questions.toList()

	fun updateQuestions(newQuestions: Collection<QuizQuestion>) {
		newQuestions
			.filter { it.id != Constants.UNSAVED_ID }
			.filter { target -> questions.any { it.id == target.id } }
			.forEach { target ->
				val resentQuestion = questions.first { target.id == it.id }
				if (target.quizType != resentQuestion.quizType) {
					throw QuizTypeMissMatchException()
				}

				questions.clear()
				questions.addAll(newQuestions)
			}
	}

	fun grade(
		questionId: Long,
		sheet: AnswerSheet,
	): GradeResult {
		val question: QuizQuestion =
			questions.find { it.id == questionId } ?: throw NotFoundQuestionException(questionId)

		return GradeResult(question.isCorrect(sheet))
	}

	fun gradeAll(sheets: Map<Long, AnswerSheet>): Map<Long, GradeResult> {
		if (sheets.size != questions.size) {
			throw IllegalSubmitSheetFormatException()
		}

		return sheets.mapValues { (id, sheet) ->
			GradeResult(
				questions.find { it.id == id }?.isCorrect(sheet)
					?: throw NotFoundQuestionException(id),
			)
		}
	}

	fun getAnswer(questionId: Long): QuestionAnswer {
		val quizQuestion: QuizQuestion =
			questions.find { it.id == questionId } ?: throw NotFoundQuestionException(questionId)

		return quizQuestion.answer
	}
}