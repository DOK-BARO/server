package kr.kro.dokbaro.server.core.bookquiz.domain.answerstyle

import kr.kro.dokbaro.server.core.bookquiz.domain.AnswerSheet
import kr.kro.dokbaro.server.core.bookquiz.domain.Answerable
import kr.kro.dokbaro.server.core.bookquiz.domain.QuizType
import kr.kro.dokbaro.server.core.bookquiz.domain.exception.IllegalRegisterSheetFormatException
import kr.kro.dokbaro.server.core.bookquiz.domain.exception.IllegalSubmitSheetFormatException

class OXAnswer private constructor(
	private val answer: OX,
) : Answerable {
	companion object {
		fun from(sheet: AnswerSheet): Answerable {
			if (sheet.answer.size != 1 ||
				!OX.entries.map { it.name }.contains(sheet.answer.first())
			) {
				throw IllegalRegisterSheetFormatException()
			}

			return OXAnswer(OX.valueOf(sheet.answer.first()))
		}
	}

	override fun match(sheet: AnswerSheet): Boolean {
		if (sheet.answer.size != 1 ||
			!OX.entries.map { it.name }.contains(sheet.answer.first())
		) {
			throw IllegalSubmitSheetFormatException()
		}

		return answer.name == sheet.answer.first()
	}

	override fun getType(): QuizType = QuizType.OX

	override fun getAnswers(): Collection<String> = listOf(answer.name)
}

enum class OX {
	O,
	X,
}