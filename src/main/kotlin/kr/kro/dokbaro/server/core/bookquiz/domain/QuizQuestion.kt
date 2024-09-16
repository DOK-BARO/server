package kr.kro.dokbaro.server.core.bookquiz.domain

import kr.kro.dokbaro.server.common.constant.Constants

data class QuizQuestion(
	val content: String,
	val explanation: String,
	val answer: Answerable,
	val answerOptions: List<AnswerOption> = listOf(), // 답안 선택지
	val id: Long = Constants.UNSAVED_ID,
) {
	fun getQuizType(): QuizType = answer.getType()
}