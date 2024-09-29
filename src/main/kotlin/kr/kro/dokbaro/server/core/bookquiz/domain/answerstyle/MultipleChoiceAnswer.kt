package kr.kro.dokbaro.server.core.bookquiz.domain.answerstyle

import kr.kro.dokbaro.server.core.bookquiz.domain.AnswerOptionId
import kr.kro.dokbaro.server.core.bookquiz.domain.AnswerSheet
import kr.kro.dokbaro.server.core.bookquiz.domain.Answerable
import kr.kro.dokbaro.server.core.bookquiz.domain.QuizType
import kr.kro.dokbaro.server.core.bookquiz.domain.exception.IllegalRegisterSheetFormatException
import kr.kro.dokbaro.server.core.bookquiz.domain.exception.IllegalSubmitSheetFormatException

class MultipleChoiceAnswer private constructor(
	private val answers: Set<AnswerOptionId>,
) : Answerable {
	companion object {
		fun from(sheet: AnswerSheet): Answerable {
			val nums: Collection<Long?> = sheet.answer.map { it.toLongOrNull() }

			if (nums.contains(null)) {
				throw IllegalRegisterSheetFormatException()
			}

			return MultipleChoiceAnswer(nums.map { AnswerOptionId(it!!) }.toSet())
		}
	}

	override fun match(sheet: AnswerSheet): Boolean {
		val submitAnswers: Collection<Long?> = sheet.answer.map { it.toLongOrNull() }

		if (submitAnswers.contains(null)) {
			throw IllegalSubmitSheetFormatException()
		}

		if (answers.size != submitAnswers.size) {
			return false
		}

		return answers.map { it.value }.containsAll(submitAnswers)
	}

	override fun getType(): QuizType = QuizType.MULTIPLE_CHOICE

	override fun getAnswers(): Collection<String> = answers.map { it.value.toString() }
}