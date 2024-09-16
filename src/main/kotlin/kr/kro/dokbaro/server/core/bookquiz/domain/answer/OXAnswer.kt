package kr.kro.dokbaro.server.core.bookquiz.domain.answer

import kr.kro.dokbaro.server.core.bookquiz.domain.AnswerSheet
import kr.kro.dokbaro.server.core.bookquiz.domain.Answerable
import kr.kro.dokbaro.server.core.bookquiz.domain.QuizType

data class OXAnswer(
	val answer: OX,
) : Answerable {
	override fun isCorrect(sheet: AnswerSheet): Boolean {
		if (sheet.answer.size != 1) {
			throw RuntimeException()
		}

		return answer.name == sheet.answer.first()
	}

	override fun getType(): QuizType = QuizType.OX
}

enum class OX {
	O,
	X,
}