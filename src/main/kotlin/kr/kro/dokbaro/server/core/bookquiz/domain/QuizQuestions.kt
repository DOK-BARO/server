package kr.kro.dokbaro.server.core.bookquiz.domain

import kr.kro.dokbaro.server.common.constant.Constants
import kr.kro.dokbaro.server.core.bookquiz.domain.exception.QuizTypeMissMatchException

data class QuizQuestions(
	private val questions: MutableList<QuizQuestion> = mutableListOf(),
) {
	fun getQuestions(): List<QuizQuestion> = questions.toList()

	fun updateQuestions(newQuestions: Collection<QuizQuestion>) {
		newQuestions
			.filter { it.id != Constants.UNSAVED_ID }
			.filter { target -> questions.any { it.id == target.id } }
			.forEach { target ->
				val resentQuestion = questions.first { target.id == it.id }
				if (target.quizType != resentQuestion.quizType) {
					throw QuizTypeMissMatchException()
				}

				questions.clear()
				questions.addAll(newQuestions)
			}
	}
}