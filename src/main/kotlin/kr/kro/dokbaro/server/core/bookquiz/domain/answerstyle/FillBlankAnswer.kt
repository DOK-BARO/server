package kr.kro.dokbaro.server.core.bookquiz.domain.answerstyle

import kr.kro.dokbaro.server.core.bookquiz.domain.AnswerSheet
import kr.kro.dokbaro.server.core.bookquiz.domain.Answerable
import kr.kro.dokbaro.server.core.bookquiz.domain.QuizType

class FillBlankAnswer private constructor(
	private val answers: Collection<String>,
) : Answerable {
	companion object {
		fun from(sheet: AnswerSheet): Answerable = FillBlankAnswer(sheet.answer)
	}

	override fun match(sheet: AnswerSheet): Boolean = answers == sheet.answer

	override fun getType(): QuizType = QuizType.FILL_BLANK

	override fun getAnswers(): Collection<String> = answers
}