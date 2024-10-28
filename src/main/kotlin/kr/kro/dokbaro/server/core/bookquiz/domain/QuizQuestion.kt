package kr.kro.dokbaro.server.core.bookquiz.domain

import kr.kro.dokbaro.server.common.constant.Constants

data class QuizQuestion(
	val content: String,
	// 답안 선택지
	val selectOptions: Collection<SelectOption> = emptyList(),
	val answer: QuestionAnswer,
	val active: Boolean = true,
	val id: Long = Constants.UNSAVED_ID,
) {
	val quizType: QuizType = answer.gradeSheet.getType()

	fun isCorrect(sheet: AnswerSheet): Boolean = answer.gradeSheet.isCorrect(sheet)
}