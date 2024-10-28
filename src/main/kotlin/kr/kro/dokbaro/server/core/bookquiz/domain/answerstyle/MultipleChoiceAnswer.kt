package kr.kro.dokbaro.server.core.bookquiz.domain.answerstyle

import kr.kro.dokbaro.server.core.bookquiz.domain.AnswerSheet
import kr.kro.dokbaro.server.core.bookquiz.domain.Gradable
import kr.kro.dokbaro.server.core.bookquiz.domain.QuizType
import kr.kro.dokbaro.server.core.bookquiz.domain.exception.IllegalRegisterSheetFormatException
import kr.kro.dokbaro.server.core.bookquiz.domain.exception.IllegalSubmitSheetFormatException

class MultipleChoiceAnswer private constructor(
	private val correctAnswers: Set<Int>,
) : Gradable {
	companion object {
		fun from(sheet: AnswerSheet): Gradable {
			if (sheet.answer.any { it.toIntOrNull() == null }) {
				throw IllegalRegisterSheetFormatException()
			}

			val nums: Set<Int> = sheet.answer.map { it.toInt() }.toSet()

			return MultipleChoiceAnswer(nums)
		}
	}

	override fun isCorrect(sheet: AnswerSheet): Boolean {
		if (sheet.answer.any { it.toIntOrNull() == null }) {
			throw IllegalSubmitSheetFormatException()
		}
		val submitAnswers: Set<Int> = sheet.answer.map { it.toInt() }.toSet()

		if (correctAnswers.size != submitAnswers.size) {
			return false
		}

		return correctAnswers.containsAll(submitAnswers)
	}

	override fun getType(): QuizType = QuizType.MULTIPLE_CHOICE

	override fun getAnswers(): Collection<String> = correctAnswers.map { it.toString() }
}