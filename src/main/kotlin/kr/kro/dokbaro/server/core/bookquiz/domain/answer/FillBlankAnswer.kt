package kr.kro.dokbaro.server.core.bookquiz.domain.answer

import kr.kro.dokbaro.server.core.bookquiz.domain.AnswerSheet
import kr.kro.dokbaro.server.core.bookquiz.domain.Answerable
import kr.kro.dokbaro.server.core.bookquiz.domain.QuizType

data class FillBlankAnswer(
	val answers: List<String>,
) : Answerable {
	override fun isCorrect(sheet: AnswerSheet): Boolean = answers == sheet.answer

	override fun getType(): QuizType = QuizType.FILL_BLANK
}