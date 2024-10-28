package kr.kro.dokbaro.server.core.bookquiz.domain.answerstyle

import kr.kro.dokbaro.server.core.bookquiz.domain.AnswerSheet
import kr.kro.dokbaro.server.core.bookquiz.domain.Gradable
import kr.kro.dokbaro.server.core.bookquiz.domain.QuizType
import kr.kro.dokbaro.server.core.bookquiz.domain.exception.IllegalRegisterSheetFormatException
import kr.kro.dokbaro.server.core.bookquiz.domain.exception.IllegalSubmitSheetFormatException

class OXAnswer private constructor(
	private val correctAnswers: OX,
) : Gradable {
	companion object {
		fun from(sheet: AnswerSheet): Gradable {
			if (sheet.answer.size != 1 ||
				!OX.entries.map { it.name }.contains(sheet.answer.first())
			) {
				throw IllegalRegisterSheetFormatException()
			}

			return OXAnswer(OX.valueOf(sheet.answer.first()))
		}
	}

	override fun isCorrect(sheet: AnswerSheet): Boolean {
		if (sheet.answer.size != 1 ||
			!OX.entries.map { it.name }.contains(sheet.answer.first())
		) {
			throw IllegalSubmitSheetFormatException()
		}

		return correctAnswers.name == sheet.answer.first()
	}

	override fun getType(): QuizType = QuizType.OX

	override fun getAnswers(): Collection<String> = listOf(correctAnswers.name)
}

enum class OX {
	O,
	X,
}