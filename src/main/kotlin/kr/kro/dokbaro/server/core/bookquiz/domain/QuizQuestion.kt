package kr.kro.dokbaro.server.core.bookquiz.domain

import kr.kro.dokbaro.server.common.constant.Constants

data class QuizQuestion(
	val content: String,
	// 답안 선택지
	val selectOptions: Collection<SelectOption> = emptyList(),
	val answerExplanation: String,
	// 추가 설명 이미지
	val answerExplanationImages: Collection<String> = emptyList(),
	val answer: Answerable,
	val active: Boolean = true,
	val id: Long = Constants.UNSAVED_ID,
) {
	val quizType: QuizType = answer.getType()
}