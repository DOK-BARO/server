package kr.kro.dokbaro.server.core.bookquiz.domain

import kr.kro.dokbaro.server.core.bookquiz.domain.answerstyle.FillBlankAnswer
import kr.kro.dokbaro.server.core.bookquiz.domain.answerstyle.MultipleChoiceMultipleAnswer
import kr.kro.dokbaro.server.core.bookquiz.domain.answerstyle.MultipleChoiceSingleAnswer
import kr.kro.dokbaro.server.core.bookquiz.domain.answerstyle.OXAnswer
import kr.kro.dokbaro.server.core.bookquiz.domain.answerstyle.ShortAnswer

object GradeSheetFactory {
	fun create(
		type: QuizType,
		sheet: AnswerSheet,
	): Gradable =
		when (type) {
			QuizType.FILL_BLANK -> FillBlankAnswer.from(sheet)
			QuizType.MULTIPLE_CHOICE_SINGLE_ANSWER -> MultipleChoiceSingleAnswer.from(sheet)
			QuizType.MULTIPLE_CHOICE_MULTIPLE_ANSWER -> MultipleChoiceMultipleAnswer.from(sheet)
			QuizType.OX -> OXAnswer.from(sheet)
			QuizType.SHORT -> ShortAnswer.from(sheet)
		}
}