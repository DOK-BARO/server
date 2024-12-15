package kr.kro.dokbaro.server.core.bookquiz.domain.answerstyle

import kr.kro.dokbaro.server.core.bookquiz.domain.AnswerSheet
import kr.kro.dokbaro.server.core.bookquiz.domain.Gradable
import kr.kro.dokbaro.server.core.bookquiz.domain.QuizType
import kr.kro.dokbaro.server.core.bookquiz.domain.exception.IllegalRegisterSheetFormatException
import kr.kro.dokbaro.server.core.bookquiz.domain.exception.IllegalSubmitSheetFormatException

class MultipleChoiceSingleAnswer private constructor(
	private val correctAnswers: Int,
) : Gradable {
	companion object {
		fun from(sheet: AnswerSheet): Gradable {
			if (sheet.answer.size != 1 ||
				sheet.answer.any { it.toIntOrNull() == null }
			) {
				throw IllegalRegisterSheetFormatException()
			}

			return MultipleChoiceSingleAnswer(sheet.answer.first().toInt())
		}
	}

	override fun isCorrect(sheet: AnswerSheet): Boolean {
		if (sheet.answer.size != 1 ||
			sheet.answer.any { it.toIntOrNull() == null }
		) {
			throw IllegalSubmitSheetFormatException()
		}

		return correctAnswers == sheet.answer.first().toInt()
	}

	override fun getType(): QuizType = QuizType.MULTIPLE_CHOICE_SINGLE_ANSWER

	override fun getAnswers(): Collection<String> = listOf(correctAnswers.toString())
}