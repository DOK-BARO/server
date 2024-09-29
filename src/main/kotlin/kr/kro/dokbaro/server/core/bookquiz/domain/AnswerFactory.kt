package kr.kro.dokbaro.server.core.bookquiz.domain

import kr.kro.dokbaro.server.core.bookquiz.domain.answerstyle.FillBlankAnswer
import kr.kro.dokbaro.server.core.bookquiz.domain.answerstyle.MultipleChoiceAnswer
import kr.kro.dokbaro.server.core.bookquiz.domain.answerstyle.OXAnswer
import kr.kro.dokbaro.server.core.bookquiz.domain.answerstyle.ShortAnswer

object AnswerFactory {
	fun create(
		type: QuizType,
		sheet: AnswerSheet,
	): Answerable =
		when (type) {
			QuizType.FILL_BLANK -> FillBlankAnswer.from(sheet)
			QuizType.MULTIPLE_CHOICE -> MultipleChoiceAnswer.from(sheet)
			QuizType.OX -> OXAnswer.from(sheet)
			QuizType.SHORT -> ShortAnswer.from(sheet)
		}
}