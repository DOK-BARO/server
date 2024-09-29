package kr.kro.dokbaro.server.core.bookquiz.domain

import kr.kro.dokbaro.server.common.constant.Constants

data class QuizQuestion(
	val content: String,
	// 답안 선택지
	val selectOptions: Collection<SelectOption> = emptyList(),
	val answerExplanation: String,
	val answer: Answerable,
	val quizId: Long,
	val id: Long = Constants.UNSAVED_ID,
) {
	val quizType: QuizType = answer.getType()
}