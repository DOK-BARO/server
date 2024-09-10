package kr.kro.dokbaro.server.core.bookquiz.domain.answer

import kr.kro.dokbaro.server.core.bookquiz.domain.AnswerOptionId
import kr.kro.dokbaro.server.core.bookquiz.domain.AnswerSheet
import kr.kro.dokbaro.server.core.bookquiz.domain.Answerable
import kr.kro.dokbaro.server.core.bookquiz.domain.QuizType

data class MultipleChoiceAnswer(
	val answers: Set<AnswerOptionId>,
) : Answerable {
	override fun isCorrect(sheet: AnswerSheet): Boolean {
		if (answers.size != sheet.answer.size) {
			throw RuntimeException()
		}

		val submitNumbers = sheet.answer.map { it.toIntOrNull() }
		if (submitNumbers.contains(null)) {
			throw RuntimeException()
		}

		return answers.map { it.value }.containsAll(submitNumbers)
	}

	override fun getType(): QuizType = QuizType.MULTIPLE_CHOICE
}