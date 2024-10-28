package kr.kro.dokbaro.server.core.bookquiz.domain.answerstyle

import kr.kro.dokbaro.server.core.bookquiz.domain.AnswerSheet
import kr.kro.dokbaro.server.core.bookquiz.domain.Gradable
import kr.kro.dokbaro.server.core.bookquiz.domain.QuizType

class FillBlankAnswer private constructor(
	private val correctAnswers: Collection<String>,
) : Gradable {
	companion object {
		fun from(sheet: AnswerSheet): Gradable = FillBlankAnswer(sheet.answer)
	}

	override fun isCorrect(sheet: AnswerSheet): Boolean = correctAnswers == sheet.answer

	override fun getType(): QuizType = QuizType.FILL_BLANK

	override fun getAnswers(): Collection<String> = correctAnswers
}