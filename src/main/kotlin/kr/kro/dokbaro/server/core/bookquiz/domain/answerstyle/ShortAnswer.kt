package kr.kro.dokbaro.server.core.bookquiz.domain.answerstyle

import kr.kro.dokbaro.server.core.bookquiz.domain.AnswerSheet
import kr.kro.dokbaro.server.core.bookquiz.domain.Gradable
import kr.kro.dokbaro.server.core.bookquiz.domain.QuizType
import kr.kro.dokbaro.server.core.bookquiz.domain.exception.IllegalSubmitSheetFormatException

class ShortAnswer private constructor(
	private val correctAnswers: Set<String>,
) : Gradable {
	companion object {
		fun from(sheet: AnswerSheet): Gradable = ShortAnswer(sheet.answer.toSet())
	}

	override fun isCorrect(sheet: AnswerSheet): Boolean {
		if (sheet.answer.size != 1) {
			throw IllegalSubmitSheetFormatException()
		}

		return correctAnswers.contains(sheet.answer.first())
	}

	override fun getType(): QuizType = QuizType.SHORT

	override fun getAnswers(): Collection<String> = correctAnswers
}